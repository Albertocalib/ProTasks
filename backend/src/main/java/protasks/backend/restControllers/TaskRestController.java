package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskRestController {
    interface TaskRequest extends TaskList.TaskListBasicInfo, Task.TaskListBasicInfo, Task.TaskListExtendedInfo {}

    @Autowired
    TaskListService listService;

    @Autowired
    TaskService taskService;

    @JsonView(TaskRequest.class)
    @PostMapping(value = "/newTask/board={boardName}&list={listName}&username={username}")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @PathVariable String boardName, @PathVariable String listName , @PathVariable String username){
        if(boardName == null || task==null || username==null ||listName==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> t=listService.findTaskList(username,boardName,listName);
        if (t!=null && t.size()==1){
            Task task1= new Task(task.getTitle(),task.getDescription(),t.get(0));
            taskService.save(task1);
            return new ResponseEntity<>(task1, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskRequest.class)
    @GetMapping("/username={username}")
    public ResponseEntity<List<Task>> getTasksByUsername(@PathVariable String username){
        List<Task> tasks=taskService.findByUsername(username);
        if (tasks != null){
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @JsonView(TaskRequest.class)
    @GetMapping("/taskName={name}&username={username}")
    public ResponseEntity<List<Task>> getTasksFilterByName(@PathVariable String name, @PathVariable String username){
        List<Task> tasks=taskService.filterByName(name,username);
        if (tasks != null){
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
