package protasks.backend.Task;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.File.File;
import protasks.backend.Message.Message;
import protasks.backend.Rol.Priority;
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

    public void setParent_task(Task parent_task) {
        this.parent_task = parent_task;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Task> subTasks) {
        this.subTasks = subTasks;
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

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="Description")
    private String description;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="Priority")
    private Priority priority;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="Position")
    private long position;

    @JsonView(Task.TaskListBasicInfo.class)
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
    @ManyToMany(cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Tag> tag_ids;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="date_end")
    private Date date_end;

    @JsonView(Task.TaskListBasicInfo.class)
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<File> attachments;

    @JsonView(Task.TaskListBasicInfo.class)
    @OneToMany(mappedBy = "parent_task", cascade = CascadeType.ALL)
    private List<Task> subTasks;

    @ManyToOne
    private Task parent_task;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="date_start_cycle_time")
    private Date date_start_cycle_time;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="date_end_cycle_time")
    private Date date_end_cycle_time;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="date_start_lead_time")
    private Date date_start_lead_time;

    @JsonView(Task.TaskListBasicInfo.class)
    @Column(name="date_end_lead_time")
    private Date date_end_lead_time;

    @JsonView(Task.TaskListBasicInfo.class)
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<File> attachments) {
        this.attachments = attachments;
    }

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
    public void addMessage(Message m) {
        if (this.messages==null || this.messages.isEmpty()){
            this.messages = new ArrayList<>();
        }
        if (!this.messages.contains(m)){
            this.messages.add(m);
        }
    }
    public void addSubTask(Task st) {
        if (this.subTasks==null || this.subTasks.isEmpty()){
            this.subTasks = new ArrayList<>();
        }
        if (!this.subTasks.contains(st)){
            this.subTasks.add(st);
        }
    }

    public Task getParent_task() {
        return parent_task;
    }

    public Date getDate_start_cycle_time() {
        return date_start_cycle_time;
    }

    public void setDate_start_cycle_time(Date date_start_cycle_time) {
        this.date_start_cycle_time = date_start_cycle_time;
    }

    public Date getDate_end_cycle_time() {
        return date_end_cycle_time;
    }

    public void setDate_end_cycle_time(Date date_end_cycle_time) {
        this.date_end_cycle_time = date_end_cycle_time;
    }

    public Date getDate_start_lead_time() {
        return date_start_lead_time;
    }

    public void setDate_start_lead_time(Date date_start_lead_time) {
        this.date_start_lead_time = date_start_lead_time;
    }

    public Date getDate_end_lead_time() {
        return date_end_lead_time;
    }

    public void setDate_end_lead_time(Date date_end_lead_time) {
        this.date_end_lead_time = date_end_lead_time;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        Task newTask= new Task (this.title,this.description,this.taskList,this.position);
        newTask.date_end=this.date_end;
        for (Tag t : this.tag_ids){
            newTask.addTag(t);
        }
        return newTask;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}

