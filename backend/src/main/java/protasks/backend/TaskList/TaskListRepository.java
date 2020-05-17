package protasks.backend.TaskList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import protasks.backend.Board.Board;
import protasks.backend.user.User;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByBoard(Board b);

    @Query(value = "SELECT tl.* FROM task_list tl " +
            "join board b on b.id=tl.board_id " +
            "join boards_users_rel bur on bur.board_id=b.id " +
            "join user u on bur.user_id=u.id " +
            "WHERE (LOWER(u.username) = LOWER(:usernameOrEmail) " +
            "or LOWER(u.email) = LOWER(:usernameOrEmail)) " +
            "and LOWER(b.name)=LOWER(:board) " +
            "and LOWER(tl.title)=LOWER(:list)=", nativeQuery = true)
    List<TaskList> findTaskList(@Param("username") String username, @Param("board") String board, @Param("list") String list);

}
