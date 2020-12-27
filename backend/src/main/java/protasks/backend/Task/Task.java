package protasks.backend.Task;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.Tag.Tag;
import protasks.backend.TaskList.TaskList;
import protasks.backend.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Task implements Comparable<Task>,Cloneable {
    @Override
    public int compareTo(Task o) {
        return Long.compare(this.position, o.getPosition());
    }
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

    @JsonView(Task.TaskListBasicInfo.class)
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

    @JsonView(Task.TaskListBasicInfo.class)
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Tag> tag_ids;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="date_end")
    private Date date_end;

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

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
        this.position = taskList.getTasks().size()+1;
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

    public void setPosition(long position) {
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

    public List<Tag> getTag_ids() {
        return tag_ids;
    }

    public void setTag_ids(List<Tag> tag_ids) {
        this.tag_ids = tag_ids;
    }

    public void removeTag(Tag t) {
        if (!this.tag_ids.isEmpty()){
            this.tag_ids.remove(t);
        }
    }
    public void addTag(Tag t) {
        if (this.tag_ids==null || this.tag_ids.isEmpty()){
            this.tag_ids = new ArrayList<>();
        }
        if (!this.tag_ids.contains(t)){
            this.tag_ids.add(t);
        }
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        Task newTask= new Task (this.title,this.description,this.taskList,this.position);
        for (Tag t : this.tag_ids){
            newTask.addTag(t);
        }
        return newTask;
    }

}

