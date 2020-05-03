package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardRestController {
    interface BoardsRequest extends User.UserBasicInfo, Board.BoardBasicInfo, Board.BoardDetailsInfo,BoardUsersPermRel.BoardBasicInfo{}

    @Autowired
    BoardService boardService;

    @JsonView(BoardsRequest.class)
    @GetMapping("/username={username}")
    public ResponseEntity<List<Board>> getBoards(@PathVariable String username){
        List<Board> boards=boardService.findByUsername(username);
        if (boards != null){
            return new ResponseEntity<>(boards, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @JsonView(BoardsRequest.class)
    @GetMapping("/boardName={name}&username={username}")
    public ResponseEntity<List<Board>> getBoardsFilterByName(@PathVariable String name,@PathVariable String username){
        List<Board> boards=boardService.filterByName(name,username);
        if (boards != null){
            return new ResponseEntity<>(boards, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
