package protasks.backend.restControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Task.Task;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;

import java.util.ArrayList;
import java.util.List;

import static protasks.backend.Rol.Rol.OWNER;

@RestController
@RequestMapping("/api/list")
public class TaskListRestController {
    interface BoardsRequest extends User.UserBasicInfo, Board.BoardBasicInfo, Board.BoardDetailsInfo,BoardUsersPermRel.BoardBasicInfo{}
    interface TaskListRequest extends TaskList.TaskListExtendedInfo,TaskList.TaskListBasicInfo, Task.TaskListBasicInfo{}
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
        List<Board> b=boardService.filterBoardsByNameUnique(boardName,username);
        if (b!=null && b.size()==1){
            TaskList t= new TaskList(list.getTitle(),b.get(0));
            listService.save(t);
            return new ResponseEntity<>(t, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @JsonView(TaskListRequest.class)
    @GetMapping(value = "/board={boardName}&username={username}")
    public ResponseEntity<List<TaskList>> getTaskListsByBoard(@PathVariable String boardName, @PathVariable String username){
        if(boardName == null || username==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<TaskList> tl=listService.findTasksListsByBoardName(username,boardName);
        if (tl!=null){
            return new ResponseEntity<>(tl, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.CREATED);
    }

    @JsonView(TaskList.TaskListBasicInfo.class)
    @PutMapping(value = "/id={id}&position={position}")
    public ResponseEntity<TaskList> updatePosition(@PathVariable Long id, @PathVariable Long position){
        if(id == null || position==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TaskList tl=listService.findById(id);
        if (tl!=null){
            updatePositions(tl,position);
            return new ResponseEntity<>(tl, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    private void updatePositions(TaskList tl,long newPosition){
        List<TaskList> lists=tl.getBoard().getTaskLists();
        lists.sort(TaskList::compareTo);
        int initialPosition;
        int finalPosition;
        int incr;
        if (tl.getPosition()<newPosition){
            initialPosition=(int)tl.getPosition()-1;
            finalPosition=(int) newPosition;
            incr=-1;
        }else{
            finalPosition=(int)tl.getPosition();
            initialPosition=(int) newPosition-1;
            incr=1;
        }
        for (int i = initialPosition; i <finalPosition ; i++) {
            TaskList tln=lists.get(i);
            tln.setPosition(tln.getPosition()+incr);
            this.listService.save(tln);
        }
        tl.setPosition(newPosition);
        this.listService.save(tl);
    }

    @JsonView(TaskListRequest.class)
    @GetMapping(value = "/id={id}")
    public ResponseEntity<List<TaskList>> getTaskBoardListsByListId(@PathVariable("id") Long id){
        if(id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Board b=boardService.findBoardByTaskListId(id);
        if (b!=null){
            return new ResponseEntity<>(b.getTaskLists(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.CREATED);
    }


}
