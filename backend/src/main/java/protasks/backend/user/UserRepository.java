package protasks.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "SELECT * FROM user u WHERE LOWER(u.username) = LOWER(:usernameOrEmail) or LOWER(u.email) = LOWER(:usernameOrEmail)",nativeQuery = true)
    User findByUsernameOrEmailCustom(@Param("usernameOrEmail") String usernameOrEmail);
    User findByUsernameOrEmail(String username,String email);
}
