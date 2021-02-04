package protasks.backend.TaskList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import protasks.backend.Board.Board;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByBoard(Board b);

    @Query(value = "SELECT tl.* FROM task_list tl " +
            "join board b on b.id=tl.board_id " +
            "join boards_users_rel bur on bur.board_id=b.id " +
            "join user u on bur.user_id=u.id " +
            "WHERE (LOWER(u.username) = LOWER(:username) " +
            "or LOWER(u.email) = LOWER(:username)) " +
            "and LOWER(b.name)=LOWER(:board) " +
            "and LOWER(tl.title)=LOWER(:list)", nativeQuery = true)
    List<TaskList> findTaskList(@Param("username") String username, @Param("board") String board, @Param("list") String list);

    @Query(value = "SELECT tl.* FROM task_list tl " +
            "join board b on b.id=tl.board_id " +
            "join boards_users_rel bur on bur.board_id=b.id " +
            "join user u on bur.user_id=u.id " +
            "WHERE (LOWER(u.username) = LOWER(:username) " +
            "or LOWER(u.email) = LOWER(:username)) " +
            "and LOWER(b.name)=LOWER(:board) order by tl.position asc", nativeQuery = true)
    List<TaskList> findTaskListByBoardName(@Param("username") String username, @Param("board") String board);

    TaskList findById(long id);

    @Query(value = "SELECT tl.* FROM task_list tl " +
            "join board b on b.id=tl.board_id " +
            "WHERE b.id=:board_id and LOWER(tl.title)=LOWER(:list)", nativeQuery = true)
    List<TaskList> findTaskList(@Param("board_id") Long board_id, @Param("list") String list);


}
