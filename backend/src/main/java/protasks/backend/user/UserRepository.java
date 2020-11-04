package protasks.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import protasks.backend.Board.Board;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "SELECT * FROM user u WHERE LOWER(u.username) = LOWER(:usernameOrEmail) or LOWER(u.email) = LOWER(:usernameOrEmail)",nativeQuery = true)
    User findByUsernameOrEmailCustom(@Param("usernameOrEmail") String usernameOrEmail);
    User findByUsernameOrEmail(String username,String email);
    @Query(value = "SELECT u.* FROM user u join boards_users_rel bur on bur.user_id=u.id where bur.board_id=:id",nativeQuery = true)
    List<User> findByBoardId(Long id);

}
