package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardRestController {

    @Autowired
    BoardService boardService;

    @JsonView(Board.class)
    @GetMapping("/{username}")
    public ResponseEntity<List<Board>> getBoards(@PathVariable String username){
        List<Board> boards=boardService.findByUsername(username);
        if (boards != null){
            return new ResponseEntity<>(boards, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
