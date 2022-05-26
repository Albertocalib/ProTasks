package protasks.backend.RestControllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Message.Message;
import protasks.backend.Message.MessageService;
import protasks.backend.Tag.Tag;
import protasks.backend.Tag.TagService;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/message")
public class MessageRestController {
    interface MessageRequest extends User.UserBasicInfo, Message.MessageBasicInfo,Message.MessageExtendedInfo {
    }
    @Autowired
    MessageService messageService;

    @JsonView(MessageRequest.class)
    @PostMapping("/newMessage")
    public ResponseEntity<Message> newMessage(@RequestBody Message message) {
        if(message == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        message.setCreate_date(new Date());
        messageService.save(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }



}