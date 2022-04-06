package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Board.BoardUsersPermService;
import protasks.backend.Rol.Rol;
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

import static protasks.backend.Rol.Rol.OWNER;

@RestController
@RequestMapping("/api/board")
public class BoardRestController {
    interface BoardsRequest extends User.UserBasicInfo, Board.BoardBasicInfo, Board.BoardDetailsInfo, BoardUsersPermRel.BoardBasicInfo, TaskList.TaskListBasicInfo {
    }

    interface BoardsUserRequest extends User.UserBasicInfo, Board.BoardBasicInfo, BoardUsersPermRel.BoardBasicInfo, TaskList.TaskListBasicInfo {
    }

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    TaskListService taskListService;

    @Autowired
    TaskService taskService;

    @Autowired
    BoardUsersPermService boardUsersPermService;

    @JsonView(BoardsRequest.class)
    @GetMapping("/username={username}")
    public ResponseEntity<List<Board>> getBoards(@PathVariable String username) {
        List<Board> boards = boardService.findByUsername(username);
        if (boards != null) {
            return new ResponseEntity<>(boards, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @JsonView(BoardsRequest.class)
    @GetMapping("/boardName={name}&username={username}")
    public ResponseEntity<List<Board>> getBoardsFilterByName(@PathVariable String name, @PathVariable String username) {
        List<Board> boards = boardService.filterByName(name, username);
        if (boards != null) {
            return new ResponseEntity<>(boards, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @JsonView(Board.BoardBasicInfo.class)
    @PostMapping(value = "/newBoard/username={username}")
    public ResponseEntity<Board> createBoard(@RequestBody Board board, @PathVariable String username) {
        if (board == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User u = userService.findByUsernameOrEmailCustom(username);
        if (u != null) {
            Board b = new Board(board.getName(), board.getPhoto());
            BoardUsersPermRel bs = new BoardUsersPermRel(b, u, OWNER);
            b.addUser(bs);
            u.addBoard(bs);
            boardService.save(b);
            userService.save(u);
            return new ResponseEntity<>(b, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @PutMapping("/id={id}/wipActivated={wipActivated}&wipLimit={wipLimit}&wipList={wipList}")
    public ResponseEntity<Board> updateWip(@PathVariable("id") Long id, @PathVariable("wipActivated") Boolean wipActivated, @PathVariable("wipLimit") int wipLimit, @PathVariable("wipList") String wipList) {
        Optional<Board> b = boardService.findById(id);
        if (b.isPresent()) {
            Board board = b.get();
            board.setWipActivated(wipActivated);
            board.setWipLimit(wipLimit);
            board.setWipList(wipList);
            boardService.save(board);
            return new ResponseEntity<>(board, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @PutMapping("/id={id}/username={username}&rol{rol}")
    public ResponseEntity<Board> addUserToBoard(@PathVariable("id") Long id, @PathVariable("username") String username, @PathVariable("rol") Rol rol) {
        Optional<Board> b = boardService.findById(id);
        User u = userService.findByUsernameOrEmailCustom(username);
        if (b.isPresent() && u != null) {
            Board board = b.get();
            BoardUsersPermRel bs = new BoardUsersPermRel(board, u, rol);
            board.addUser(bs);
            boardService.save(board);
            return new ResponseEntity<>(board, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @PutMapping("/id={id}/userId={userId}&role={role}")
    public ResponseEntity<Board> updateRole(@PathVariable("id") Long id, @PathVariable("userId") Long userId, @PathVariable("role") Rol role) {
        BoardUsersPermRel b = boardUsersPermService.findBoardPermByUserIdAndBoardId(id, userId);
        if (b != null) {
            b.setRol(role);
            boardUsersPermService.save(b);
            return new ResponseEntity<>(b.getBoard(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @DeleteMapping("/id={id}/userId={userId}")
    public ResponseEntity<Board> deleteUserFromBoard(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        BoardUsersPermRel b = boardUsersPermService.findBoardPermByUserIdAndBoardId(id, userId);
        if (b != null) {
            boardUsersPermService.delete(b);
            Optional<Board> board = boardService.findById(id);
            if (board.isPresent()) {
                return new ResponseEntity<>(b.getBoard(), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @GetMapping("/id={id}/userId={userId}")
    public ResponseEntity<BoardUsersPermRel> getRol(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        BoardUsersPermRel b = boardUsersPermService.findBoardPermByUserIdAndBoardId(id, userId);
        if (b != null) {
            return new ResponseEntity<>(b, HttpStatus.CREATED);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @PutMapping("/id={id}/timeActivated={timeActivated}&cycleStart={cycleStart}&cycleEnd={cycleEnd}&leadStart={leadStart}&leadEnd={leadEnd}")
    public ResponseEntity<Board> updateTime(@PathVariable("id") Long id, @PathVariable("timeActivated") Boolean timeActivated,
                                           @PathVariable("cycleStart") String cycleStart, @PathVariable("cycleEnd") String cycleEnd,
                                           @PathVariable("leadStart") String leadStart, @PathVariable("leadEnd") String leadEnd) {
        Optional<Board> b = boardService.findById(id);
        if (b.isPresent()) {
            Board board = b.get();
            if (timeActivated){
                if (!board.getCycleStartList().equals(cycleStart)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),cycleStart);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_start_cycle_time(new Date());
                            this.taskService.save(t);
                        }
                    }
                    board.setCycleStartList(cycleStart);
                }
                if (!board.getCycleEndList().equals(cycleEnd)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),cycleEnd);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_end_cycle_time(new Date());
                            this.taskService.save(t);
                        }
                    }
                    board.setCycleEndList(cycleEnd);
                }
                if (!board.getLeadStartList().equals(leadStart)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),leadStart);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_start_lead_time(new Date());
                            this.taskService.save(t);
                        }
                    }
                    board.setLeadStartList(leadStart);
                }
                if (!board.getLeadEndList().equals(leadEnd)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),leadEnd);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_end_lead_time(new Date());
                            this.taskService.save(t);
                        }
                    }
                    board.setLeadEndList(cycleEnd);
                }
            }
            board.setTimeActivated(timeActivated);
            boardService.save(board);
            return new ResponseEntity<>(board, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
