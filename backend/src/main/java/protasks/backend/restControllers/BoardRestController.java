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
import protasks.backend.TaskList.TaskList;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

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

    @JsonView(Board.BoardBasicInfo.class)
    @PutMapping("/id={id}/wipActivated={wipActivated}&wipLimit={wipLimit}&wipList={wipList}")
    public ResponseEntity<Board> updateTaskPosition(@PathVariable("id") Long id, @PathVariable("wipActivated") Boolean wipActivated, @PathVariable("wipLimit") int wipLimit, @PathVariable("wipList") String wipList) {
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
        User u =userService.findByUsernameOrEmailCustom(username);
        if (b.isPresent() && u!=null){
            Board board =b.get();
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
        BoardUsersPermRel b = boardUsersPermService.findBoardPermByUserIdAndBoardId(id,userId);
        if (b!=null) {
            b.setRol(role);
            boardUsersPermService.save(b);
            return new ResponseEntity<>(b.getBoard(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
