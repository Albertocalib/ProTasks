package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
    interface UserRequest extends User.UserBasicInfo, User.UserDetailsInfo,Board.BoardBasicInfo, BoardUsersPermRel.UserBasicInfo{}


    @Autowired
    UserService userService;

    @JsonView(User.UserBasicInfo.class)
    @PostMapping("/logIn")
    public ResponseEntity<User> logIn(String username, String password){
        User u=userService.findByUsernameOrEmailCustom(username);
        if (u != null && new BCryptPasswordEncoder().matches(password,u.getPassword())){
            return new ResponseEntity<>(u, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @JsonView(User.UserBasicInfo.class)
    @PostMapping(value = "/register/newUser")
    public ResponseEntity<User> register(@RequestBody User newUser){
        if(newUser == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User userCheckup = userService.findByUsernameOrEmail(newUser.getUsername(),newUser.getEmail());
        if(userCheckup != null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        userService.save(newUser);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    @JsonView(UserRequest.class)
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        User user = userService.findByUsernameOrEmailCustom(username);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @JsonView(User.UserBasicInfo.class)
    @PutMapping(value = "/updatePhoto/{username}")
    public ResponseEntity<User> updatePhoto(@RequestBody String photo, @PathVariable String username){
        User user = userService.findByUsernameOrEmailCustom(username);
        if (user==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setWrite_date();
        user.setPhoto(photo);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
