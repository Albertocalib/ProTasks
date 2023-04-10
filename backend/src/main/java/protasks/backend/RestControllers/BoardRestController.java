package protasks.backend.RestControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Board.BoardUsersPermService;
import protasks.backend.File.File;
import protasks.backend.File.FileService;
import protasks.backend.Rol.Rol;
import protasks.backend.Tag.Tag;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.*;

import static protasks.backend.Rol.Rol.OWNER;

@RestController
@RequestMapping("/api/board")
public class BoardRestController {
    interface BoardsRequest extends User.UserBasicInfo, Board.BoardBasicInfo, Board.BoardDetailsInfo, BoardUsersPermRel.BoardBasicInfo, TaskList.TaskListBasicInfo,Task.TaskListBasicInfo,Tag.TagBasicInfo, File.FileBasicInfo {
    }

    interface BoardsUserRequest extends User.UserBasicInfo, Board.BoardBasicInfo, BoardUsersPermRel.BoardBasicInfo, TaskList.TaskListBasicInfo,Task.TaskListBasicInfo, Tag.TagBasicInfo, File.FileBasicInfo {
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

    @Autowired
    FileService fileService;

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
            BoardUsersPermRel bs = new BoardUsersPermRel(board, u, OWNER);
            board.addUser(bs);
            u.addBoard(bs);
            board.setWrite_date();
            board.setCreate_date();
            u.setWrite_date();
            boardService.save(board);
            userService.save(u);
            return new ResponseEntity<>(board, HttpStatus.CREATED);
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
            board.setWrite_date();
            boardService.save(board);
            return new ResponseEntity<>(board, HttpStatus.OK);
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
            board.setWrite_date();
            u.setWrite_date();
            boardService.save(board);
            return new ResponseEntity<>(board, HttpStatus.OK);
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
            return new ResponseEntity<>(b.getBoard(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @DeleteMapping("/id={id}/userId={userId}")
    public ResponseEntity<Board> deleteUserFromBoard(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        BoardUsersPermRel b = boardUsersPermService.findBoardPermByUserIdAndBoardId(id, userId);
        if (b != null) {
            boardUsersPermService.delete(b);
            return new ResponseEntity<>(b.getBoard(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(BoardsUserRequest.class)
    @GetMapping("/id={id}/userId={userId}")
    public ResponseEntity<BoardUsersPermRel> getRol(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        BoardUsersPermRel b = boardUsersPermService.findBoardPermByUserIdAndBoardId(id, userId);
        if (b != null) {
            return new ResponseEntity<>(b, HttpStatus.OK);

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
                if (cycleStart!=null && !Objects.equals(board.getCycleStartList(), cycleStart)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),cycleStart);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_start_cycle_time(new Date());
                            t.setWrite_date();
                            this.taskService.save(t);
                        }
                    }
                    board.setCycleStartList(cycleStart);
                }
                if (cycleEnd!=null && !Objects.equals(board.getCycleEndList(), cycleEnd)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),cycleEnd);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_end_cycle_time(new Date());
                            t.setWrite_date();
                            this.taskService.save(t);
                        }
                    }
                    board.setCycleEndList(cycleEnd);
                }
                if (leadStart!=null && !Objects.equals(board.getLeadStartList(), leadStart)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),leadStart);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_start_lead_time(new Date());
                            t.setWrite_date();
                            this.taskService.save(t);
                        }
                    }
                    board.setLeadStartList(leadStart);
                }
                if (leadEnd!=null && !Objects.equals(board.getLeadEndList(), leadEnd)){
                    List<TaskList> list_taskList = this.taskListService.findTaskList(board.getId(),leadEnd);
                    for (TaskList l:list_taskList) {
                        List<Task> tasks = l.getTasks();
                        for (Task t:tasks){
                            t.setDate_end_lead_time(new Date());
                            t.setWrite_date();
                            this.taskService.save(t);
                        }
                    }
                    board.setLeadEndList(cycleEnd);
                }
            }
            board.setTimeActivated(timeActivated);
            board.setWrite_date();
            boardService.save(board);
            return new ResponseEntity<>(board, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long id) {
        Optional<Board> board = boardService.findById(id);
        if (board.isPresent()) {
            boardService.delete(board.get());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @JsonView(Board.BoardBasicInfo.class)
    @PostMapping(value = "copyBoard/boardId={boardId}")
    public ResponseEntity<Board> copyBoard(@PathVariable("boardId") Long boardId) throws CloneNotSupportedException {
        if (boardId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Board> b = boardService.findById(boardId);
        if (b.isPresent()) {
            Board newBoard = b.get().clone();
            this.boardService.save(newBoard);
            return new ResponseEntity<>(newBoard, HttpStatus.CREATED);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

    @JsonView(Board.BoardBasicInfo.class)
    @PutMapping(value = "updateBoard/{boardId}")
    public ResponseEntity<Board> updateBoard(@RequestBody Board board, @PathVariable("boardId") Long boardId) {
        if (boardId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Board> b = boardService.findById(boardId);
        if (b.isPresent()) {
            Board boardFind = b.get();
            File updatedFIle = board.getFile_id();
            File file = boardFind.getFile_id();
            file.setContent(updatedFIle.getContent());
            file.setName(updatedFIle.getName());
            file.setType(updatedFIle.getType());
            boardFind.setPhoto(updatedFIle.getContent());
            boardFind.setName(board.getName());
            this.fileService.save(file);
            this.boardService.save(boardFind);
            return new ResponseEntity<>(boardFind, HttpStatus.CREATED);

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

}
