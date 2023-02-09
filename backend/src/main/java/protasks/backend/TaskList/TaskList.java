package protasks.backend.TaskList;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.Board.Board;
import protasks.backend.Task.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class TaskList implements Comparable<TaskList>,Cloneable {
    @Override
    public int compareTo(TaskList o) {
        return Long.compare(this.position, o.getPosition());
    }

    public interface TaskListBasicInfo{}
    public interface TaskListExtendedInfo{}
    @JsonView(TaskList.TaskListBasicInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(TaskList.TaskListBasicInfo.class)
    @Column(name="title")
    private String title;

    @JsonView(TaskList.TaskListExtendedInfo.class)
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL)
    @OrderBy("position asc")
    private List<Task> tasks;

    @JsonView(TaskList.TaskListExtendedInfo.class)
    @ManyToOne
    private Board board;

    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;

    @Column(name="position")
    private long position;

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setWrite_date(Date write_date) {
        this.write_date = write_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public TaskList(String title, Board board) {
        this.title = title;
        this.board = board;
        this.create_date = new Date();
        this.write_date =new Date();
        this.tasks=new ArrayList<>();
        this.position=board.getTaskLists().size()+1;
    }

    public TaskList() {
        this.create_date = new Date();
        this.write_date =new Date();
        this.tasks=new ArrayList<>();
        this.position=0;
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

    public void addTask(Task t){this.tasks.add(t);}

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        TaskList newTaskList= new TaskList (this.title,this.board);
        newTaskList.setPosition(this.position);
        for (Task t:this.tasks) {
            Task nt= (Task) t.clone();
            nt.setTaskList(newTaskList);
            newTaskList.addTask(nt);
        }
        return newTaskList;
    }




}
