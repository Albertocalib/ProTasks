package protasks.backend.RestControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.File.File;
import protasks.backend.File.FileService;
import protasks.backend.Message.Message;
import protasks.backend.Rol.Priority;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/task")
public class TaskRestController {
    interface TaskRequest extends TaskList.TaskListBasicInfo, Task.TaskListBasicInfo, Task.TaskListExtendedInfo,File.FileBasicInfo, Message.MessageBasicInfo, User.UserBasicInfo {
    }

    interface UserRequest extends User.UserBasicInfo, User.UserDetailsInfo, Board.BoardBasicInfo, BoardUsersPermRel.UserBasicInfo {
    }
    interface TaskRequestCopyOrMove extends TaskList.TaskListBasicInfo, TaskList.TaskListBoard, Board.BoardBasicInfo, Task.TaskListBasicInfo, Task.TaskListExtendedInfo,File.FileBasicInfo, Message.MessageBasicInfo, User.UserBasicInfo {
    }

    @Autowired
    TaskListService listService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @JsonView(TaskRequest.class)
    @PostMapping(value = "/newTask/listId={listId}")
    public ResponseEntity<Task> createTaskByListId(@RequestBody Task task, @PathVariable Long listId) {
        if (listId == null || task == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TaskList t = listService.findById(listId);
        if (t != null) {
            task.setTaskList(t);
            Board b = t.getBoard();
            if (b.getTimeActivated()!=null && b.getTimeActivated()) {
                updateTaskTime(t, task, b);
            }
            taskService.save(task);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskRequest.class)
    @PostMapping(value = "/newTask/board={boardName}&list={listName}&username={username}")
    public ResponseEntity<Task> createTask(@RequestBody Task task, @PathVariable String boardName, @PathVariable String listName, @PathVariable String username) {
        if (boardName == null || task == null || username == null || listName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> t = listService.findTaskList(username, boardName, listName);
        if (t != null && t.size() == 1) {
            TaskList list = t.get(0);
            task.setTaskList(list);
            Board b = t.get(0).getBoard();
            if (b.getTimeActivated()!=null && b.getTimeActivated()) {
                updateTaskTime(list, task, b);
            }
            taskService.save(task);
            return new ResponseEntity<>(task, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/id={taskId}")
    public ResponseEntity<Boolean> deleteTaskById(@PathVariable Long taskId) {
        if (taskId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task t = taskService.findById(taskId);
        if (t!= null) {
            updatePositions(t, null, 0, true);
            this.taskService.delete(t);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private void updateTaskTime(TaskList list, Task task1, Board b) {
        if (b.getCycleStartList()!=null && b.getCycleStartList().equals(list.getTitle())) {
            task1.setDate_start_cycle_time(new Date());
        }
        if (b.getCycleEndList()!=null && b.getCycleEndList().equals(list.getTitle())) {
            task1.setDate_end_cycle_time(new Date());
        }
        if (b.getLeadStartList()!=null && b.getLeadStartList().equals(list.getTitle())) {
            task1.setDate_start_lead_time(new Date());
        }
        if (b.getLeadEndList()!=null && b.getLeadEndList().equals(list.getTitle())) {
            task1.setDate_end_lead_time(new Date());
        }
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
                updatePositions(t, null, 0, true);
                TaskList tl = listService.findById(newTaskList);
                if (tl != null) {
                    //Update new List
                    updatePositions(t, tl, newPosition, false);
                    Board b = t.getTaskList().getBoard();
                    if (b.getTimeActivated()!=null && b.getTimeActivated()){
                        updateTaskTime(tl, t, b);
                        this.taskService.save(t);
                    }
                }
            } else {
                //update list
                updatePositions(t, null, newPosition, false);
            }

            return new ResponseEntity<>(t, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void updatePositions(Task t, TaskList taskListNew, long newPosition, boolean eraseMode) {
        List<Task> lists;
        if (taskListNew != null) {
            lists = taskListNew.getTasks();
        } else {
            lists = t.getTaskList().getTasks();
        }
        lists.sort(Task::compareTo);
        if (eraseMode) {
            lists.remove(t);
            for (int i = 0; i < lists.size(); i++) {
                Task task = lists.get(i);
                task.setPosition(i + 1);
                this.taskService.save(task);
            }
        } else {
            int initialPosition;
            int finalPosition;
            int incr;
            if (t.getPosition() < newPosition && taskListNew == null) {
                initialPosition = (int) t.getPosition() - 1;
                finalPosition = (int) newPosition;
                incr = -1;
            } else {
                if (taskListNew != null) {
                    finalPosition = lists.size();
                } else {
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
            if (taskListNew != null) {
                t.setTaskList(taskListNew);
            }
            this.taskService.save(t);
        }
    }

    @JsonView(UserRequest.class)
    @GetMapping("users/task_id={id}")
    public ResponseEntity<List<User>> getUsersByTaskId(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task != null) {
            return new ResponseEntity<>(task.getUsers(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("id={id}/user={user_id}")
    public ResponseEntity<Boolean> addUserToTask(@PathVariable Long id, @PathVariable Long user_id) {
        Optional<User> u = userService.findById(user_id);
        if (u.isPresent()) {
            Task task = taskService.findById(id);
            if (task != null) {
                User user = u.get();
                user.addTask(task);
                userService.save(user);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("id={id}/user={user_id}")
    public ResponseEntity<Boolean> deleteUserToTask(@PathVariable Long id, @PathVariable Long user_id) {
        Optional<User> u = userService.findById(user_id);
        if (u.isPresent()) {
            Task task = taskService.findById(id);
            if (task != null) {
                User user = u.get();
                user.removeTask(task);
                userService.save(user);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @JsonView(TaskRequest.class)
    @PutMapping("id={id}&newDateEnd={newDate}")
    public ResponseEntity<Task> updateDateEnd(@PathVariable Long id, @PathVariable Date newDate) {
        Task task = taskService.findById(id);
        if (task != null && newDate != null) {
            task.setDate_end(newDate);
            taskService.save(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(TaskRequest.class)
    @PutMapping("id={id}&newTitle={title}")
    public ResponseEntity<Task> updateTitleTask(@PathVariable Long id, @PathVariable String title) {
        Task task = taskService.findById(id);
        if (task != null && title != null) {
            task.setTitle(title);
            taskService.save(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(TaskRequest.class)
    @PutMapping("id={id}&newDescription={description}")
    public ResponseEntity<Task> updateDescriptionTask(@PathVariable Long id, @PathVariable String description) {
        Task task = taskService.findById(id);
        if (task != null && description != null) {
            task.setDescription(description);
            taskService.save(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(TaskRequest.class)
    @PutMapping(value = "id={taskId}&listDestName={listDestName}&boardDestId={boardDestId}&username={username}")
    public ResponseEntity<Task> moveTask(@PathVariable("taskId") Long taskId,
                                            @PathVariable("listDestName") String listDestName,
                                            @PathVariable("boardDestId") Long boardDestId,
                                            @PathVariable("username") String username) throws CloneNotSupportedException {
        if (taskId == null || listDestName == null || boardDestId == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task t = taskService.findById(taskId);
        if (t != null) {
            if (!listDestName.equals(t.getTaskList().getTitle())) {
                List<TaskList> tl = listService.findTaskList(boardDestId, listDestName);
                if (tl != null && tl.size() == 1) {
                    //Update old list (eraseMode=True)
                    updatePositions(t, null, 0, true);
                    return updateTaskListAndPosition(t, tl.get(0));
                }
            }
        }


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    private ResponseEntity<Task> updateTaskListAndPosition(Task t, TaskList tl) {
        t.setPosition(tl.getTasks().size()+1);
        t.setTaskList(tl);
        taskService.save(t);
        tl.addTask(t);
        listService.save(tl);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @JsonView(TaskRequest.class)
    @PostMapping(value = "id={taskId}&listDestName={listDestName}&boardDestId={boardDestId}&username={username}")
    public ResponseEntity<Task> copyTask(@PathVariable("taskId") Long taskId,
                                            @PathVariable("listDestName") String listDestName,
                                            @PathVariable("boardDestId") Long boardDestId,
                                            @PathVariable("username") String username) throws CloneNotSupportedException {
        if (taskId == null || listDestName == null || boardDestId == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task t = taskService.findById(taskId);
        if (t != null) {
            Task t2 = (Task)t.clone();
            if (t2!=null) {
                List<TaskList> tl = listService.findTaskList(boardDestId, listDestName);
                if (tl != null && tl.size() == 1) {
                    return updateTaskListAndPosition(t2, tl.get(0));
                }

            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

    @JsonView(TaskRequestCopyOrMove.class)
    @PostMapping(value = "id={taskId}&taskListId={listDestId}")
    public ResponseEntity<Task> copyTaskById(@PathVariable("taskId") Long taskId,
                                            @PathVariable("listDestId") Long listDestId) throws CloneNotSupportedException {
        if (taskId == null || listDestId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task t = taskService.findById(taskId);
        if (t != null) {
            Task t2 = (Task)t.clone();
            if (t2!=null) {
                TaskList tl = listService.findById(listDestId);
                if (tl != null) {
                    return updateTaskListAndPosition(t2, tl);
                }

            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @JsonView(TaskRequestCopyOrMove.class)
    @PutMapping(value = "id={taskId}&taskListId={listDestId}")
    public ResponseEntity<Task> moveTaskById(@PathVariable("taskId") Long taskId,
                                            @PathVariable("listDestId") Long listDestId) {
        if (taskId == null || listDestId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task t = taskService.findById(taskId);
        if (t != null) {
            if (t.getTaskList().getId()!=listDestId) {
                TaskList tl = listService.findById(listDestId);
                if (tl != null) {
                    //Update old list (eraseMode=True)
                    updatePositions(t, null, 0, true);
                    return updateTaskListAndPosition(t, tl);
                }
            }
        }


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @JsonView(TaskRequest.class)
    @PostMapping(value = "newAttachments/task={taskId}")
    public ResponseEntity<Task> addAttachments(@PathVariable("taskId")Long taskId, @RequestBody HashSet<File> files){
        Task t = taskService.findById(taskId);
        if (t!=null){
            for (File f:files) {
                File newFile = new File(f.getName(),f.getContent(),f.getType(),t);
                fileService.save(newFile);
            }
            t = taskService.findById(taskId);
            return new ResponseEntity<>(t,HttpStatus.CREATED);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskRequest.class)
    @DeleteMapping("id={taskId}/fileId={fileId}")
    public ResponseEntity<Task> removeAttachment(@PathVariable("taskId") Long taskId, @PathVariable("fileId")Long fileId){
        File f = fileService.findById(fileId);
        if (f!=null){
            fileService.delete(f);
            Task t = taskService.findById(taskId);
            return new ResponseEntity<>(t,HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskRequest.class)
    @PostMapping(value = "newSubTask/task={taskId}")
    public ResponseEntity<Task> addSubtasks(@PathVariable("taskId")Long taskId, @RequestBody Task subTask){

        Task t = taskService.findById(taskId);
        if (t!=null){
            subTask.setParent_task(t);
            taskService.save(subTask);
            t.addSubTask(subTask);
            return new ResponseEntity<>(t,HttpStatus.CREATED);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskRequest.class)
    @DeleteMapping("ids={taskIds}")
    public ResponseEntity<Task> removeSubtasks(@PathVariable("taskIds") String taskIdsStr){
        String[] taskIds = taskIdsStr.split("&");
        Task t = null;
        for (String id:taskIds) {
            Task subtask = null;
            try {
                subtask = taskService.findById(Long.parseLong(id));
            }catch (NumberFormatException ex){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (subtask==null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (t==null){
                t=subtask.getParent_task();
            }
            taskService.delete(subtask);
        }
        if (t!=null){
            return new ResponseEntity<>(t,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @JsonView(TaskRequest.class)
    @PutMapping("id={id}&newPriority={priority}")
    public ResponseEntity<Task> updatePriorityTask(@PathVariable Long id, @PathVariable Priority priority) {
        Task task = taskService.findById(id);
        if (task != null && priority != null) {
            task.setPriority(priority);
            taskService.save(task);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
