package protasks.backend.TaskList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import protasks.backend.Board.Board;
import protasks.backend.user.User;

import java.util.List;


@Service
public class TaskListService {

    @Autowired
    TaskListRepository taskListRepository;

    public List<TaskList> findByBoard(Board b){
        return this.taskListRepository.findByBoard(b);
    }
}

