package protasks.backend.Board;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.Tag.Tag;
import protasks.backend.TaskList.TaskList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Board {
    public interface BoardBasicInfo{}
    public interface BoardDetailsInfo{}
    @JsonView(BoardBasicInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(BoardBasicInfo.class)
    @Column(name="Name")
    private String name;

    @JsonView(BoardBasicInfo.class)
    @Column (name="Photo",columnDefinition="MEDIUMBLOB")
    private String photo;

    @JsonView(BoardDetailsInfo.class)
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "board")
    private List<TaskList> taskLists;

    @JsonView(Board.BoardBasicInfo.class)
    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BoardUsersPermRel> users;

    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @JsonView(BoardBasicInfo.class)
    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;

    @JsonView(BoardDetailsInfo.class)
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "board")
    private List<Tag> tags;

    @JsonView(BoardBasicInfo.class)
    @Column(name="wipActivated")
    private Boolean wipActivated;

    @JsonView(BoardBasicInfo.class)
    @Column(name="wipLimit")
    private int wipLimit;

    @JsonView(BoardBasicInfo.class)
    @Column(name="wipList")
    private String wipList;

    @JsonView(BoardBasicInfo.class)
    @Column(name="timeActivated")
    private Boolean timeActivated;

    @JsonView(BoardBasicInfo.class)
    @Column(name="cycleStartList")
    private String cycleStartList;

    @JsonView(BoardBasicInfo.class)
    @Column(name="cycleEndList")
    private String cycleEndList;

    @JsonView(BoardBasicInfo.class)
    @Column(name="leadStartList")
    private String leadStartList;

    @JsonView(BoardBasicInfo.class)
    @Column(name="leadEndList")
    private String leadEndList;

    public Boolean getTimeActivated() {
        return timeActivated;
    }

    public void setTimeActivated(Boolean timeActivated) {
        this.timeActivated = timeActivated;
    }

    public String getCycleStartList() {
        return cycleStartList;
    }

    public void setCycleStartList(String cycleStartList) {
        this.cycleStartList = cycleStartList;
    }

    public String getCycleEndList() {
        return cycleEndList;
    }

    public void setCycleEndList(String cycleEndList) {
        this.cycleEndList = cycleEndList;
    }

    public String getLeadStartList() {
        return leadStartList;
    }

    public void setLeadStartList(String leadStartList) {
        this.leadStartList = leadStartList;
    }

    public String getLeadEndList() {
        return leadEndList;
    }

    public void setLeadEndList(String leadEndList) {
        this.leadEndList = leadEndList;
    }

    public String getWipList() {
        return wipList;
    }

    public void setWipList(String wipList) {
        this.wipList = wipList;
    }

    public Boolean getWipActivated() {
        return wipActivated;
    }

    public void setWipActivated(Boolean wipActivated) {
        this.wipActivated = wipActivated;
    }

    public int getWipLimit() {
        return wipLimit;
    }

    public void setWipLimit(int wipLimit) {
        this.wipLimit = wipLimit;
    }

    public long getId() {
        return id;
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
        this.create_date=new Date();
        this.write_date =new Date();
        this.taskLists = new ArrayList<>();
        this.tags= new ArrayList<>();
    }
    public Board(){}

    public Date getWrite_date() {
        return write_date;
    }

    public void setWrite_date() {
        this.write_date = new Date();
    }

    public Date getCreate_date() {
        return create_date;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
