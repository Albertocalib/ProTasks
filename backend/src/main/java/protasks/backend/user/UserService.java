package protasks.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findByUsernameOrEmailCustom(String name){
        return this.userRepository.findByUsernameOrEmailCustom(name);
    }
    public User findByUsernameOrEmail(String username,String email){
        return this.userRepository.findByUsernameOrEmail(username,email);
    }
    public void save(User user){
        this.userRepository.save(user);
    }
}

