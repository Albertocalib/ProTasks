package protasks.backend.RestControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.File.File;
import protasks.backend.Message.Message;
import protasks.backend.Tag.Tag;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/list")
public class TaskListRestController {
    interface TaskListRequestExt extends TaskListRequest,User.UserDetailsInfo {
    }

    interface TaskListRequest extends TaskList.TaskListExtendedInfo, TaskList.TaskListBasicInfo, Task.TaskListBasicInfo, Board.BoardBasicInfo, BoardUsersPermRel.BoardBasicInfo,User.UserBasicInfo, File.FileBasicInfo,Tag.TagBasicInfo, Message.MessageBasicInfo {
    }
    interface TaskListBasicRequest extends TaskList.TaskListBasicInfo,TaskList.TaskListExtendedInfo,Board.BoardBasicInfo, BoardUsersPermRel.BoardBasicInfo,User.UserBasicInfo{
    }

    @Autowired
    TaskListService listService;

    @Autowired
    BoardService boardService;

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;


    @JsonView(TaskList.TaskListBasicInfo.class)
    @PostMapping(value = "/newList/board={boardName}&username={username}")
    public ResponseEntity<TaskList> createTaskList(@RequestBody TaskList list, @PathVariable String boardName, @PathVariable String username) {
        if (boardName == null || list == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Board> b = boardService.filterBoardsByNameUnique(boardName, username);
        if (b != null && b.size() == 1) {
            list.setBoard(b.get(0));
            list.setCreate_date(new Date());
            listService.save(list);
            return new ResponseEntity<>(list, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskList.TaskListBasicInfo.class)
    @PostMapping(value = "/newList/boardId={boardId}")
    public ResponseEntity<TaskList> createTaskListByBoardId(@RequestBody TaskList list, @PathVariable Long boardId) {
        if (boardId == null || list == null ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Board> b = boardService.findById(boardId);
        if (b.isPresent()) {
            list.setBoard(b.get());
            list.setCreate_date(new Date());
            listService.save(list);
            return new ResponseEntity<>(list, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskListRequest.class)
    @GetMapping(value = "/board={boardName}&username={username}")
    public ResponseEntity<List<TaskList>> getTaskListsByBoard(@PathVariable String boardName, @PathVariable String username) {
        if (boardName == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> tl = listService.findTasksListsByBoardName(username, boardName);
        if (tl != null) {
            return new ResponseEntity<>(tl, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }
    @JsonView(TaskListRequestExt.class)
    @GetMapping(value = "/boardId={boardId}")
    public ResponseEntity<List<TaskList>> getTaskListsByBoardId(@PathVariable Long boardId) {
        if (boardId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> tl = listService.findTaskListsByBoardId(boardId);
        if (tl != null) {
            return new ResponseEntity<>(tl, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }

    @JsonView(TaskList.TaskListBasicInfo.class)
    @PutMapping(value = "/id={id}&position={position}")
    public ResponseEntity<TaskList> updatePosition(@PathVariable Long id, @PathVariable Long position) {
        if (id == null || position == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TaskList tl = listService.findById(id);
        if (tl != null) {
            updatePositions(tl, position, "update");
            return new ResponseEntity<>(tl, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void updatePositions(TaskList tl, long newPosition, String mode) {
        List<TaskList> lists = tl.getBoard().getTaskLists();
        if (mode.equals("delete")) {
            lists.remove(tl);
            this.listService.delete(tl);
        }
        lists.sort(TaskList::compareTo);
        int initialPosition;
        int finalPosition;
        int incr;
        if (tl.getPosition() < newPosition) {
            initialPosition = (int) tl.getPosition() - 1;
            finalPosition = (int) newPosition;
            incr = -1;
        } else {
            finalPosition = (int) tl.getPosition();
            initialPosition = (int) newPosition - 1;
            incr = 1;
        }
        for (int i = initialPosition; i < finalPosition; i++) {
            if (i < lists.size()) {
                TaskList tln = lists.get(i);
                if (tln != tl) {
                    tln.setPosition(tln.getPosition() + incr);
                    tln.setWrite_date();
                    this.listService.save(tln);
                }
            }
        }
        if (mode.equals("update")) {
            tl.setPosition(newPosition);
            tl.setWrite_date();
            this.listService.save(tl);
        }
    }

    @JsonView(TaskListRequest.class)
    @GetMapping(value = "/id={id}")
    public ResponseEntity<List<TaskList>> getTaskBoardListsByListId(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Board b = boardService.findBoardByTaskListId(id);
        if (b != null) {
            return new ResponseEntity<>(b.getTaskLists(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
    }

    @JsonView(TaskListRequest.class)
    @DeleteMapping(value = "/board={boardName}&list={listName}&username={username}")
    public ResponseEntity<Boolean> deleteTaskList(@PathVariable("boardName") String boardName, @PathVariable("listName") String listName, @PathVariable("username") String username) {
        if (boardName == null || username == null || listName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> tl = listService.findTaskList(username, boardName, listName);
        if (tl != null && tl.size() == 1) {
            updatePositions(tl.get(0), tl.get(0).getBoard().getTaskLists().size(), "delete");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @JsonView(TaskListRequest.class)
    @DeleteMapping(value = "/id={listId}")
    public ResponseEntity<Boolean> deleteTaskListById(@PathVariable Long listId) {
        if (listId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TaskList tl = listService.findById(listId);
        if (tl != null) {
            updatePositions(tl, tl.getBoard().getTaskLists().size(), "delete");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @JsonView(TaskListBasicRequest.class)
    @PutMapping(value = "id={tasklistId}&boardDestId={boardDestId}")
    public ResponseEntity<TaskList> MoveTaskList(@PathVariable("tasklistId") Long tasklistId, @PathVariable("boardDestId") Long boardDestId) {
        if (tasklistId == null || boardDestId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Board> b = boardService.findById(boardDestId);
        if (b.isPresent()) {
            TaskList tl = listService.findById(tasklistId);
            if (tl != null) {
                updatePositions(tl, tl.getBoard().getTaskLists().size(), "delete");

                tl.setPosition(b.get().getTaskLists().size() + 1);
                tl.setBoard(b.get());
                listService.save(tl);
                return new ResponseEntity<>(tl, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "name={listName}&board={boardName}&boardDestId={boardDestId}&username={username}")
    public ResponseEntity<Boolean> moveTaskList(@PathVariable("boardName") String boardName, @PathVariable("listName") String listName, @PathVariable("boardDestId") Long boardDestId, @PathVariable("username") String username) {
        if (boardName == null || listName == null || boardDestId == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Board> b = boardService.findById(boardDestId);
        if (b.isPresent()) {
            List<TaskList> tl = listService.findTaskList(username, boardName, listName);
            if (tl != null && tl.size() == 1) {
                TaskList t = tl.get(0);
                updatePositions(t, t.getBoard().getTaskLists().size(), "delete");

                t.setPosition(b.get().getTaskLists().size() + 1);
                t.setBoard(b.get());
                listService.save(tl.get(0));
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

    @JsonView(TaskListRequest.class)
    @PostMapping(value = "name={listName}&board={boardName}&boardDestId={boardDestId}&username={username}")
    public ResponseEntity<Boolean> copyTaskList(@PathVariable("boardName") String boardName, @PathVariable("listName") String listName, @PathVariable("boardDestId") Long boardDestId, @PathVariable("username") String username) throws CloneNotSupportedException {
        if (boardName == null || listName == null || boardDestId == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Board> b = boardService.findById(boardDestId);
        if (b.isPresent()) {
            List<TaskList> tl = listService.findTaskList(username, boardName, listName);
            if (tl != null && tl.size() == 1) {
                TaskList t = tl.get(0);
                this.cloneTaskList(t,b);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }
    private TaskList cloneTaskList(TaskList tl,Optional<Board> b) throws CloneNotSupportedException {
        TaskList ntl= (TaskList) tl.clone();
        ntl.setPosition(b.get().getTaskLists().size() + 1);
        ntl.setBoard(b.get());
        ntl.setCreate_date(new Date());
        listService.save(ntl);
        List<Task> tasks=ntl.getTasks();
        for (Task t0:tl.getTasks()) {
            if (!t0.getUsers().isEmpty()) {
                Optional<Task> nt = tasks.stream().filter(taskI -> taskI.getTitle().equals(t0.getTitle())).findFirst();
                if (nt.isPresent()) {
                    for (User u : t0.getUsers()) {
                        u.addTask(nt.get());
                        u.setWrite_date();
                        this.userService.save(u);
                    }
                }
            }
        }
        return ntl;
    }

    @JsonView(TaskListRequest.class)
    @PostMapping(value = "id={tasklistId}&boardDestId={boardDestId}")
    public ResponseEntity<TaskList> copyTaskList(@PathVariable("tasklistId") Long tasklistId, @PathVariable("boardDestId") Long boardDestId) throws CloneNotSupportedException {
        if (tasklistId == null || boardDestId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Board> b = boardService.findById(boardDestId);
        if (b.isPresent()) {
            TaskList tl = listService.findById(tasklistId);
            if (tl != null) {
                TaskList ntl = this.cloneTaskList(tl,b);
                return new ResponseEntity<>(ntl, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

}
