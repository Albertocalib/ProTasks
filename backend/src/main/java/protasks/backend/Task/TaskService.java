package protasks.backend.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import protasks.backend.Board.Board;
import protasks.backend.TaskList.TaskList;

import java.util.List;


@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;


    public void save(Task t) {
        this.taskRepository.save(t);
    }

    public List<Task> findByTaskList(TaskList t ) {
        return this.taskRepository.findByTaskList(t);
    }
    public List<Task> findByUsername(String username){
        return this.taskRepository.findByUsername(username);
    }

    public List<Task> filterByName(String name,String username){return this.taskRepository.filterTasksByName(name,username);}

}

