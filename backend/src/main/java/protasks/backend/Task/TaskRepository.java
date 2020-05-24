package protasks.backend.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import protasks.backend.Board.Board;
import protasks.backend.TaskList.TaskList;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskList(TaskList t);

}
