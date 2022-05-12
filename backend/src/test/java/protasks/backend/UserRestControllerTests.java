package protasks.backend;

import com.fasterxml.jackson.annotation.JsonView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.RestControllers.UserRestController;
import protasks.backend.user.User;
import protasks.backend.user.UserRepository;
import protasks.backend.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static protasks.backend.Rol.Rol.ADMIN;
import static protasks.backend.Rol.Rol.OWNER;

@SpringBootTest
class UserRestControllerTests {

    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private UserService userService;


    @Test
    void testUserWrongUsername() {
        MockitoAnnotations.initMocks(this);
        String name = "Wrong User";
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(null);
        ResponseEntity<User> response = userRestController.logIn(name,"123456789");
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void testUserWrongPassword() {
        MockitoAnnotations.initMocks(this);
        String name = "AlbertoCalib";
        String password = "Wrong Password";
        User user = new User();
        user.setName(name);
        user.setPassword("Good Password");
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(user);
        ResponseEntity<User> response = userRestController.logIn(name, password);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void testUserLoginOk() {
        MockitoAnnotations.initMocks(this);
        String name = "Albertocalib";
        String password = "Good Password";
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(user);
        ResponseEntity<User> response = userRestController.logIn(name, password);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
    }
    @Test
    void testRegisterUserNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<User> response = userRestController.register(null);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }
    @Test
    void testRegisterUserOk() {
        MockitoAnnotations.initMocks(this);
        String name = "Albertocalib";
        String password = "Good Password";
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(user);
        ResponseEntity<User> response = userRestController.logIn(name, password);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
    }
    @Test
    void testRegisterUserCheckExist() {
        MockitoAnnotations.initMocks(this);
        String name = "Albertocalib";
        String password = "Password";
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(null);
        ResponseEntity<User> response = userRestController.register(user);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.CREATED.value());
    }

    @Test
    void testGetUserNoExist() {
        MockitoAnnotations.initMocks(this);
        String name = "Albertocalib12";
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(null);
        ResponseEntity<User> response = userRestController.getUser(name);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }
    @Test
    void testGetUserExist() {
        MockitoAnnotations.initMocks(this);
        String name = "Albertocalib";
        User u = new User();
        u.setName(name);
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(u);
        ResponseEntity<User> response = userRestController.getUser(name);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
    }

    @Test
    void testUpdatePhotoUserExist() {
        MockitoAnnotations.initMocks(this);
        String name = "Albertocalib";
        String photo= "Photo_Base_64";
        User u = new User();
        u.setName(name);
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(u);
        ResponseEntity<User> response = userRestController.updatePhoto(photo,name);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()).getPhoto(), photo);
    }
    @Test
    void testUpdatePhotoUserNoExist() {
        MockitoAnnotations.initMocks(this);
        String name = "Albertocalib";
        String photo= "Photo_Base_64";
        User u = new User();
        u.setName(name);
        Mockito.when(userService.findByUsernameOrEmailCustom(name)).thenReturn(null);
        ResponseEntity<User> response = userRestController.updatePhoto(photo,name);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testGetUsersInBoardIdNoOkOrNoUsers() {
        MockitoAnnotations.initMocks(this);
        long board_id = 1;
        Mockito.when(userService.findByBoardId(board_id)).thenReturn(null);
        ResponseEntity<List<User>> response = userRestController.getUsersInBoard(board_id);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.BAD_REQUEST.value());
    }
    @Test
    void testGetUsersInBoardIdOk() {
        MockitoAnnotations.initMocks(this);
        long board_id = 1;
        User u1 = new User();
        User u2 = new User();
        Board board = new Board();
        board.setId(board_id);
        BoardUsersPermRel bs = new BoardUsersPermRel(board, u1, OWNER);
        BoardUsersPermRel bs2 = new BoardUsersPermRel(board, u2, ADMIN);
        board.addUser(bs);
        board.addUser(bs2);
        List<User> userInBoard=new ArrayList<>();
        for (BoardUsersPermRel u :board.getUsers()){
            userInBoard.add(u.getUser());
        }
        Mockito.when(userService.findByBoardId(board_id)).thenReturn(userInBoard);
        ResponseEntity<List<User>> response = userRestController.getUsersInBoard(board_id);
        Assertions.assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
    }

}
