package protasks.backend.Task;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.TaskList.TaskList;
import protasks.backend.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Task {
    public interface TaskListBasicInfo{}
    public interface TaskListExtendedInfo{}

    @JsonView(Task.TaskListBasicInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="Title")
    private String title;

    @Column(name="Description")
    private String description;
    @Column(name="Position")
    private long position;
    @ManyToMany(mappedBy = "tasks")
    private List<User> users ;

    @JsonView(Task.TaskListExtendedInfo.class)
    @ManyToOne
    private TaskList taskList;

    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;

    public Task() {
        this.create_date=new Date();
        this.write_date =new Date();
    }
    public Task(String title,String description,TaskList list,long position) {
        this.title=title;
        this.description=description;
        this.position=position;
        this.create_date=new Date();
        this.taskList=list;
        this.write_date =new Date();
    }
    public Task(String title,String description,TaskList list) {
        this.title=title;
        this.description=description;
        this.create_date=new Date();
        this.write_date =new Date();
        this.taskList=list;
        this.position = taskList.getTasks().size();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public Date getWrite_date() {
        return write_date;
    }

    public void setWrite_date() {
        this.write_date = new Date();
    }

    public Date getCreate_date() {
        return create_date;
    }

}

