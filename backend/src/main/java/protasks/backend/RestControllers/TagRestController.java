package protasks.backend.RestControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Tag.Tag;
import protasks.backend.Tag.TagService;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.user.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tag")
public class TagRestController {
    interface TagsRequest extends User.UserBasicInfo, Board.BoardBasicInfo, Board.BoardDetailsInfo, BoardUsersPermRel.BoardBasicInfo, TaskList.TaskListBasicInfo {
    }

    @Autowired
    TaskService taskService;

    @Autowired
    TagService tagService;

    @Autowired
    BoardService boardService;

    @JsonView(Tag.TagBasicInfo.class)
    @GetMapping("/tags/task_id={id}")
    public ResponseEntity<List<Tag>> getTagsByTaskId(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task != null) {
            return new ResponseEntity<>(task.getTag_ids(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/id={id}/task={task_id}")
    public ResponseEntity<Boolean> deleteUserToTask(@PathVariable Long id, @PathVariable Long task_id) {
        Optional<Tag> tag = tagService.findTagById(id);
        if (tag.isPresent()) {
            Task t = taskService.findById(task_id);
            if (t!=null) {
                t.removeTag(tag.get());
                taskService.save(t);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/id={id}/tag={tag_id}")
    public ResponseEntity<Boolean> addTagToTask(@PathVariable Long id,@PathVariable Long tag_id) {
        Task task = taskService.findById(id);
        if (task!=null){
            Optional<Tag> t = tagService.findTagById(tag_id);
            if (t.isPresent()) {
                Tag tag=t.get();
                task.addTag(tag);
                task.setWrite_date();
                taskService.save(task);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @JsonView(Tag.TagBasicInfo.class)
    @GetMapping("/board_id={id}")
    public ResponseEntity<List<Tag>> getTagsInBoard(@PathVariable long id){
        List<Tag> tags = tagService.findByBoardId(id);
        if(tags == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @JsonView(Tag.TagBasicInfo.class)
    @PostMapping("/newTag/board={boardName}&username={username}")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag, @PathVariable("boardName") String boardName, @PathVariable("username") String username){
        if (boardName == null || tag == null || username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Board> b = boardService.filterBoardsByNameUnique(boardName,username);
        if (b != null && b.size() == 1) {
            tag.setBoard(b.get(0));
            tagService.save(tag);
            return new ResponseEntity<>(tag, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}