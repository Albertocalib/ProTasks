package protasks.backend.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @PostMapping("/logIn")
    public ResponseEntity<User> logIn(String username, String password){
        User u=userService.findByUsernameOrEmailCustom(username);
        if (u != null && new BCryptPasswordEncoder().matches(password,u.getPassword())){
            return new ResponseEntity<>(u, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

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

}
