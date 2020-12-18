package protasks.backend.Tag;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.Board.Board;
import protasks.backend.Task.Task;
import protasks.backend.TaskList.TaskList;
import protasks.backend.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Tag {    

    public interface TagBasicInfo{}
    public interface TagExtendedInfo{}

    @JsonView(Tag.TagBasicInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(Tag.TagBasicInfo.class)
    @Column(name="Name")
    private String name;

    @JsonView(Tag.TagBasicInfo.class)
    @Column(name="Color")
    private String color;

    @JsonView(Tag.TagExtendedInfo.class)
    @ManyToMany(mappedBy = "tag_ids")
    private List<Task> tasks ;

    @JsonView(Tag.TagExtendedInfo.class)
    @ManyToOne
    private Board board;

    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;

    public Tag() {
        this.create_date=new Date();
        this.write_date =new Date();
    }
    public Tag(String name, String color, Board board) {
        this.name=name;
        this.color=color;
        this.create_date=new Date();
        this.board=board;
        this.write_date =new Date();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void removeTask(Task t) {
        if (!this.tasks.isEmpty()){
            this.tasks.remove(t);
        }
    }
    

}

