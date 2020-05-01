package protasks.backend.TaskList;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.Board.Board;
import protasks.backend.Task.Task;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;
    @Column(name="Title")
    private String title;
    @OneToMany(mappedBy = "taskList")
    private List<Task> tasks;
    @ManyToOne
    private Board board;

    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;

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

    public TaskList(String title,Board board) {
        this.title = title;
        this.board = board;
        this.create_date = new Date();
        this.write_date =new Date();
    }

    public Date getWrite_date() {
        return write_date;
    }

    public void setWrite_date(Date write_date) {
        this.write_date = write_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
}
