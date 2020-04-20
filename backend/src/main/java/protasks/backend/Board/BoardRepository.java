package protasks.backend.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    @Query(value = "SELECT b.* FROM board b join boards_users_rel bur on bur.board_id=b.id join user u on bur.user_id=u.id WHERE LOWER(u.username) = LOWER(:username) ",nativeQuery = true)
    List<Board> findByUsername(@Param("username") String username);
}