package protasks.backend.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import protasks.backend.TaskList.TaskList;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskList(TaskList t);
    @Query(value = "SELECT t.* FROM task t " +
            "join task_list tl on tl.id=t.task_list_id " +
            "join board b on b.id=tl.board_id " +
            "join boards_users_rel bur on bur.board_id=b.id " +
            "join user u on bur.user_id=u.id " +
            "WHERE LOWER(u.username) = LOWER(:username) or LOWER(u.email) = LOWER(:username)",nativeQuery = true)
    List<Task> findByUsername(@Param("username") String username);

    @Query(value = "SELECT t.* FROM task t " +
            "join task_list tl on tl.id=t.task_list_id "+
            "join board b on b.id=tl.board_id " +
            "join boards_users_rel bur on bur.board_id=b.id " +
            "join user u on bur.user_id=u.id " +
            "WHERE (LOWER(u.username) = LOWER(:username) or LOWER(u.email) = LOWER(:username)) and LOWER(t.title) like CONCAT(CONCAT('%',LOWER(:name)),'%')",nativeQuery = true)
    List<Task> filterTasksByName(@Param("name") String name,@Param("username") String username);
}
