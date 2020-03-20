package protasks.backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import protasks.backend.user.User;
import protasks.backend.user.UserRepository;

import javax.annotation.PostConstruct;


@Component
public class DatabaseInitializer {


	@Autowired
	private UserRepository userRepository;


	@PostConstruct
	public void init() {

		User u1= new User("Alberto","Cañal Liberal","Albertocalib_8","123456789","albertocalib@gmail.com");
		userRepository.save(u1);
	}


}
