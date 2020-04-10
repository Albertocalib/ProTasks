package protasks.backend.TaskList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import protasks.backend.Board.Board;
import protasks.backend.user.User;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList,Long> {
    List<TaskList> findByBoard(Board b);
}
