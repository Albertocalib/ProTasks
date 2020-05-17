package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.List;

import static protasks.backend.Rol.Rol.OWNER;

@RestController
@RequestMapping("/api/list")
public class TaskListRestController {
    interface BoardsRequest extends User.UserBasicInfo, Board.BoardBasicInfo, Board.BoardDetailsInfo,BoardUsersPermRel.BoardBasicInfo{}

    @Autowired
    TaskListService listService;

    @Autowired
    BoardService boardService;


    @JsonView(TaskList.TaskListBasicInfo.class)
    @PostMapping(value = "/newList/board={boardName}&username={username}")
    public ResponseEntity<TaskList> createTaskList(@RequestBody TaskList list, @PathVariable String boardName, @PathVariable String username){
        if(boardName == null || list==null || username==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Board> b=boardService.filterByName(boardName,username);
        if (b!=null && b.size()==1){
            TaskList t= new TaskList(list.getTitle(),b.get(0));
            listService.save(t);
            return new ResponseEntity<>(t, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
