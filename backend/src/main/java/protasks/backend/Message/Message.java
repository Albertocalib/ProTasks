package protasks.backend.Message;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Task.Task;
import protasks.backend.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Message {
    public interface MessageBasicInfo{}
    public interface MessageExtendedInfo{}

    @JsonView(MessageBasicInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(MessageBasicInfo.class)
    @Column(name="Body")
    private String body;

    @JsonView(MessageBasicInfo.class)
    @ManyToOne
    private User user;

    @JsonView(MessageExtendedInfo.class)
    @ManyToOne
    private Task task;

    @JsonView(MessageBasicInfo.class)
    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @JsonView(MessageBasicInfo.class)
    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;


    public Message() {
    }

    public Message(String body, User user, Task task) {
        this.body = body;
        this.user = user;
        this.task = task;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setWrite_date(Date write_date) {
        this.write_date = write_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

}
