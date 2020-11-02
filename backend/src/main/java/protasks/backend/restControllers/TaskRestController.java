package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskRestController {
    interface TaskRequest extends TaskList.TaskListBasicInfo, Task.TaskListBasicInfo, Task.TaskListExtendedInfo {
    }
    interface UserRequest extends User.UserBasicInfo, User.UserDetailsInfo, Board.BoardBasicInfo, BoardUsersPermRel.UserBasicInfo{}

    @Autowired
    TaskListService listService;

    @Autowired
    TaskService taskService;

    @JsonView(TaskRequest.class)
    @PostMapping(value = "/newTask/board={boardName}&list={listName}&username={username}")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @PathVariable String boardName, @PathVariable String listName, @PathVariable String username) {
        if (boardName == null || task == null || username == null || listName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> t = listService.findTaskList(username, boardName, listName);
        if (t != null && t.size() == 1) {
            Task task1 = new Task(task.getTitle(), task.getDescription(), t.get(0));
            taskService.save(task1);
            return new ResponseEntity<>(task1, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskRequest.class)
    @GetMapping("/username={username}")
    public ResponseEntity<List<Task>> getTasksByUsername(@PathVariable String username) {
        List<Task> tasks = taskService.findByUsername(username);
        if (tasks != null) {
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @JsonView(TaskRequest.class)
    @GetMapping("/taskName={name}&username={username}")
    public ResponseEntity<List<Task>> getTasksFilterByName(@PathVariable String name, @PathVariable String username) {
        List<Task> tasks = taskService.filterByName(name, username);
        if (tasks != null) {
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @JsonView(TaskRequest.class)
    @PutMapping("id={id}&newPosition={newPosition}&newTaskList={newTaskList}")
    public ResponseEntity<Task> updateTaskPosition(@PathVariable("id") Long id, @PathVariable("newPosition") Long newPosition, @PathVariable("newTaskList") Long newTaskList) {
        if (id == null || newPosition == null || newTaskList == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task t = taskService.findById(id);
        if (t != null) {
            if (newTaskList != t.getTaskList().getId()) {
                //Update old list (eraseMode=True)
                updatePositions(t, null,0,true);
                TaskList tl = listService.findById(newTaskList);
                if (tl != null) {
                    //Update new List
                    updatePositions(t, tl,newPosition,false);
                }
            } else {
                //update list
                updatePositions(t,null, newPosition,false);
            }

            return new ResponseEntity<>(t, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void updatePositions(Task t, TaskList taskListNew,long newPosition,boolean eraseMode) {
        List<Task> lists;
        if (taskListNew!=null){
            lists = taskListNew.getTasks();
        }else {
            lists = t.getTaskList().getTasks();
        }
        lists.sort(Task::compareTo);
        if (eraseMode) {
            for (int i = 0; i < lists.size(); i++) {
                Task task = lists.get(i);
                task.setPosition(i + 1);
                this.taskService.save(task);
            }
        } else {
            int initialPosition;
            int finalPosition;
            int incr;
            if (t.getPosition() < newPosition && taskListNew==null) {
                initialPosition = (int) t.getPosition() - 1;
                finalPosition = (int) newPosition;
                incr = -1;
            } else {
                if (taskListNew!=null){
                    finalPosition = lists.size();
                }else{
                    finalPosition = (int) t.getPosition();
                }
                initialPosition = (int) newPosition - 1;
                incr = 1;
            }
            for (int i = initialPosition; i < finalPosition; i++) {
                Task tln = lists.get(i);
                tln.setPosition(tln.getPosition() + incr);
                this.taskService.save(tln);
            }
            t.setPosition(newPosition);
            if (taskListNew!=null){
                t.setTaskList(taskListNew);
            }
            this.taskService.save(t);
        }
    }

    @JsonView(UserRequest.class)
    @GetMapping("users/task_id={id}")
    public ResponseEntity<List<User>> getUsersByTaskId(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task!= null) {
            return new ResponseEntity<>(task.getUsers(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
