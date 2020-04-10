package protasks.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Task.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @JsonView(Board.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @Column(name="Name")
    private String name;

    @Column(name="Surname")
    private String surname;

    @JsonView(Board.class)
    @Column(name="Username")
    private String username;
    @Column(name="Password")
    private String password;
    @Column(name="Email")
    private String email;
    @JoinTable(
            name = "rel_tasks_users",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="task_id", nullable = false)
    )
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BoardUsersPermRel> boardList;


    public User() {
    }
    public User(String name,String surname,String userName,String password,String email ) {
        this.name = name;
        this.surname=surname;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = userName;
        this.email=email;
        this.boardList=new ArrayList<>();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password =  new BCryptPasswordEncoder().encode(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addBoard(BoardUsersPermRel board) {
        this.boardList.add(board);
    }
}
