package protasks.backend.Task;

import protasks.backend.TaskList.TaskList;
import protasks.backend.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;
    @Column(name="Title")
    private String title;
    @Column(name="Description")
    private String description;
    @Column(name="Position")
    private long position;
    @ManyToMany(mappedBy = "tasks")
    private List<User> users ;
    @ManyToOne
    private TaskList taskList;

    public Task() {
    }
    public Task(String title,String description,long position) {
        this.title=title;
        this.description=description;
        this.position=position;
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
}

