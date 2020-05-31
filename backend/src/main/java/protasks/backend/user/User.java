package protasks.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import protasks.backend.Board.BoardUsersPermRel;
import protasks.backend.Task.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User {
    public interface UserBasicInfo{}
    public interface UserDetailsInfo{}

    @JsonView(UserBasicInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(UserBasicInfo.class)
    @Column(name="Name")
    private String name;

    @JsonView(UserBasicInfo.class)
    @Column(name="Surname")
    private String surname;

    @JsonView(UserBasicInfo.class)
    @Column(name="Username")
    private String username;

    @JsonView(UserBasicInfo.class)
    @Column(name="Password")
    private String password;

    @JsonView(UserBasicInfo.class)
    @Column(name="Email")
    private String email;

    @JoinTable(
            name = "rel_tasks_users",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="task_id", nullable = false)
    )
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Task> tasks;

    @JsonView(UserDetailsInfo.class)
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BoardUsersPermRel> boardList;

    @JsonView(UserBasicInfo.class)
    @Column (name="Photo",columnDefinition="MEDIUMBLOB")
    private String photo;

    @JsonView(UserBasicInfo.class)
    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @JsonView(UserBasicInfo.class)
    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;


    public User() {
    }
    public User(String name,String surname,String userName,String password,String email ) {
        this.name = name;
        this.surname=surname;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = userName;
        this.email=email;
        this.boardList=new ArrayList<>();
        this.create_date=new Date();
        this.write_date =new Date();
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<BoardUsersPermRel> getBoardList() {
        return boardList;
    }

    public void setBoardList(List<BoardUsersPermRel> boardList) {
        this.boardList = boardList;
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
