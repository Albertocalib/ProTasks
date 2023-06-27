package protasks.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Board.BoardUsersPermService;
import protasks.backend.RestControllers.BoardRestController;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static protasks.backend.Rol.Rol.ADMIN;

@SpringBootTest
class BoardRestControllerTests {

    @InjectMocks
    private BoardRestController boardRestController;

    @Mock
    private UserService userService;

    @Mock
    private BoardService boardService;

    @Mock
    private TaskListService taskListService;

    @Mock
    private TaskService taskService;

    @Mock
    private BoardUsersPermService boardUsersPermService;


    @Test
    void testGetBoardsNoBoards() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(boardService.findByUsername("")).thenReturn(null);
        ResponseEntity<List<Board>> response = boardRestController.getBoards("");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetBoardsOk() {
        MockitoAnnotations.initMocks(this);
        Board n = new Board();
        List<Board> boards = new ArrayList<>();
        boards.add(n);
        Mockito.when(boardService.findByUsername("")).thenReturn(boards);
        ResponseEntity<List<Board>> response = boardRestController.getBoards("");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(boards,response.getBody());
    }

    @Test
    void testGetBoardsFilterByNameNoBoards() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(boardService.filterByName("A","B")).thenReturn(null);
        ResponseEntity<List<Board>> response = boardRestController.getBoardsFilterByName("A","B");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetBoardsFilterByNameOk() {
        MockitoAnnotations.initMocks(this);
        Board n = new Board();
        List<Board> boards = new ArrayList<>();
        boards.add(n);
        Mockito.when(boardService.filterByName("A","B")).thenReturn(boards);
        ResponseEntity<List<Board>> response = boardRestController.getBoardsFilterByName("A","B");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(boards,response.getBody());
    }

    @Test
    void testCreateBoardAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<Board> response = boardRestController.createBoard(null,"B");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = boardRestController.createBoard(new Board(),null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testCreateBoardNoUser() {
        MockitoAnnotations.initMocks(this);
        Board n = new Board();
        Mockito.when(userService.findByUsernameOrEmailCustom("A")).thenReturn(null);
        ResponseEntity<Board> response = boardRestController.createBoard(n,"A");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }
    @Test
    void testCreateBoardOk() {
        MockitoAnnotations.initMocks(this);
        Board n = new Board();
        User u = new User();
        Mockito.when(userService.findByUsernameOrEmailCustom("A")).thenReturn(u);
        ResponseEntity<Board> response = boardRestController.createBoard(n,"A");
        Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());
        Assertions.assertEquals(1, n.getUsers().size());

    }

    @Test
    void testGetRolNoExist() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(boardUsersPermService.findBoardPermByUserIdAndBoardId(id,id)).thenReturn(null);
        ResponseEntity<BoardUsersPermRel> response = boardRestController.getRol(id,id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }
    @Test
    void testGetRolOk() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        BoardUsersPermRel bus = new BoardUsersPermRel();
        Mockito.when(boardUsersPermService.findBoardPermByUserIdAndBoardId(id,id)).thenReturn(bus);
        ResponseEntity<BoardUsersPermRel> response = boardRestController.getRol(id,id);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(bus,response.getBody());

    }

    @Test
    void testUpdateWipNoBoard() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(boardService.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Board> response = boardRestController.updateWip(id,true,3,"TO DO");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }
    @Test
    void testUpdateWipOk() {
        //Arrange
        MockitoAnnotations.initMocks(this);
        long id=2;
        Board b = new Board();
        Mockito.when(boardService.findById(id)).thenReturn(Optional.of(b));
        
        //Act
        ResponseEntity<Board> response = boardRestController.updateWip(id,true,3,"TO DO");

        //Assert
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,b.getWipActivated());
        Assertions.assertEquals(3,b.getWipLimit());
        Assertions.assertEquals("TO DO",b.getWipList());
    }

    @Test
    void testUpdateRoleNoExist() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(boardUsersPermService.findBoardPermByUserIdAndBoardId(id,id)).thenReturn(null);
        ResponseEntity<Board> response = boardRestController.updateRole(id,id, ADMIN);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }
    @Test
    void testUpdateRolOk() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        BoardUsersPermRel bol = new BoardUsersPermRel();
        Mockito.when(boardUsersPermService.findBoardPermByUserIdAndBoardId(id,id)).thenReturn(bol);
        ResponseEntity<Board> response = boardRestController.updateRole(id,id, ADMIN);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(ADMIN,bol.getRol());
    }
    @Test
    void testAddUserToBoardNoExistBoardOrUser() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        User u=  new User();
        Board b = new Board();
        Mockito.when(boardService.findById(id)).thenReturn(Optional.empty());
        Mockito.when(userService.findByUsernameOrEmailCustom("PRU")).thenReturn(u);
        Mockito.when(userService.findByUsernameOrEmailCustom("PRU1")).thenReturn(null);
        Mockito.when(boardService.findById(id+1)).thenReturn(Optional.of(b));
        ResponseEntity<Board> response = boardRestController.addUserToBoard(id,"PRU",ADMIN);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = boardRestController.addUserToBoard(id+1,"PRU1",ADMIN);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }
    @Test
    void testAddUserToBoardOK() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        User u=  new User();
        Board b = new Board();
        Mockito.when(boardService.findById(id)).thenReturn(Optional.of(b));
        Mockito.when(userService.findByUsernameOrEmailCustom("PRU")).thenReturn(u);
        ResponseEntity<Board> response = boardRestController.addUserToBoard(id,"PRU",ADMIN);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void testDeleteUserFromBoardNoExistBoardPerm() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(boardUsersPermService.findBoardPermByUserIdAndBoardId(id,id)).thenReturn(null);
        ResponseEntity<Board> response = boardRestController.deleteUserFromBoard(id,id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }


    @Test
    void testDeleteUserFromBoardOk() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(boardUsersPermService.findBoardPermByUserIdAndBoardId(id,id)).thenReturn(new BoardUsersPermRel());
        ResponseEntity<Board> response = boardRestController.deleteUserFromBoard(id,id);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void testUpdateTimeNoExistBoard() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(boardService.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Board> response = boardRestController.updateTime(id,true,"","","","");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }


    @Test
    void testUpdateTimeNoTImeActivatedOk() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Board b = new Board();
        Mockito.when(boardService.findById(id)).thenReturn(Optional.of(b));
        ResponseEntity<Board> response = boardRestController.updateTime(id,false,"","","","");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(false,b.getTimeActivated());
    }

    @Test
    void testUpdateTimeTImeActivatedALlParametersOfTimeAreNull() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Board b = new Board();
        Mockito.when(boardService.findById(id)).thenReturn(Optional.of(b));
        ResponseEntity<Board> response = boardRestController.updateTime(id,true,null,null,null,null);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,b.getTimeActivated());
    }
    @Test
    void testUpdateTimeUpdateCycleTimeOk() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Board b = new Board();
        b.setId(2);
        Mockito.when(boardService.findById(id)).thenReturn(Optional.of(b));
        Task t = new Task();
        Task t2 = new Task();
        TaskList tl1= new TaskList();
        TaskList tl2= new TaskList();
        t.setTaskList(tl1);
        t2.setTaskList(tl2);
        tl1.addTask(t);
        tl2.addTask(t2);
        tl1.setTitle("TO DO");
        tl1.setTitle("TO END");
        List<TaskList> list = new ArrayList<>();
        list.add(tl1);
        list.add(tl2);
        b.setTaskLists(list);
        List<TaskList> list1 = new ArrayList<>();
        list1.add(tl1);
        List<TaskList> list2 = new ArrayList<>();
        list2.add(tl2);
        Mockito.when(taskListService.findTaskList(id,"TO DO")).thenReturn(list1);
        Mockito.when(taskListService.findTaskList(id,"TO END")).thenReturn(list2);
        ResponseEntity<Board> response = boardRestController.updateTime(id,true,"TO DO","TO END",null,null);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,b.getTimeActivated());
        Assertions.assertNotNull(t.getDate_start_cycle_time());
        Assertions.assertNotNull(t2.getDate_end_cycle_time());
    }

    @Test
    void testUpdateTimeUpdateLeadTimeOk() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Board b = new Board();
        b.setId(2);
        Mockito.when(boardService.findById(id)).thenReturn(Optional.of(b));
        Task t = new Task();
        Task t2 = new Task();
        TaskList tl1= new TaskList();
        TaskList tl2= new TaskList();
        t.setTaskList(tl1);
        t2.setTaskList(tl2);
        tl1.addTask(t);
        tl2.addTask(t2);
        tl1.setTitle("TO DO");
        tl1.setTitle("TO END");
        List<TaskList> list = new ArrayList<>();
        list.add(tl1);
        list.add(tl2);
        b.setTaskLists(list);
        List<TaskList> list1 = new ArrayList<>();
        list1.add(tl1);
        List<TaskList> list2 = new ArrayList<>();
        list2.add(tl2);
        Mockito.when(taskListService.findTaskList(id,"TO DO")).thenReturn(list1);
        Mockito.when(taskListService.findTaskList(id,"TO END")).thenReturn(list2);
        ResponseEntity<Board> response = boardRestController.updateTime(id,true,null,null,"TO DO","TO END");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,b.getTimeActivated());
        Assertions.assertNotNull(t.getDate_start_lead_time());
        Assertions.assertNotNull(t2.getDate_end_lead_time());
    }

}
