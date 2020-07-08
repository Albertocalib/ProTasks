package protasks.backend.TaskList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import protasks.backend.Board.Board;

import java.util.List;


@Service
public class TaskListService {

    @Autowired
    TaskListRepository taskListRepository;

    public List<TaskList> findByBoard(Board b) {
        return this.taskListRepository.findByBoard(b);
    }

    public void save(TaskList t) {
        this.taskListRepository.save(t);
    }

    public List<TaskList> findTaskList(String username, String boardName, String list) {
        return this.taskListRepository.findTaskList(username, boardName, list);
    }
    public List<TaskList> findTasksListsByBoardName(String username,String boardName){
        return this.taskListRepository.findTaskListByBoardName(username,boardName);
    }
}

