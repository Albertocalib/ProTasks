package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Task.Task;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskRestController {
    @Autowired
    TaskListService listService;

    @JsonView(TaskList.TaskListBasicInfo.class)
    @PostMapping(value = "/newTask/board={boardName}&list={listName}&username={username}")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @PathVariable String boardName, @PathVariable String listName , @PathVariable String username){
        if(boardName == null || task==null || username==null ||listName==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> t=listService.findTaskList(username,boardName,listName);
        if (t!=null && t.size()==1){
            Task task1= new Task(task.getTitle(),task.getDescription());
            TaskList t1=t.get(0);
            t1.addTask(task1);
            listService.save(t1);
            return new ResponseEntity<>(task1, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
