package protasks.backend.Board;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.TaskList.TaskList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Board {
    @JsonView(Board.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(Board.class)
    @Column(name="Name")
    private String name;

    @JsonView(Board.class)
    @Column (name="Photo",columnDefinition="MEDIUMBLOB")
    private String photo;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "board")
    private List<TaskList> taskLists;

    @JsonView(Board.class)
    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BoardUsersPermRel> users;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<BoardUsersPermRel> getUsers() {
        return users;
    }

    public void setUsers(List<BoardUsersPermRel> users) {
        this.users = users;
    }
    public void addUser(BoardUsersPermRel user) {
        this.users.add(user);
    }

    public Board(String name, String photo) {
        this.name = name;
        this.photo = photo;
        this.users=new ArrayList<>();
    }
    public Board(){}
}