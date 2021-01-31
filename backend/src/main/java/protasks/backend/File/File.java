package protasks.backend.File;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.Task.Task;
import protasks.backend.TaskList.TaskList;

import javax.persistence.*;
import java.util.Date;

@Entity
public class File {
    public interface FileBasicInfo{}
    public interface FileExtendedInfo{}

    @JsonView(FileBasicInfo.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private long id;

    @JsonView(FileBasicInfo.class)
    @Column(name="Name")
    private String name;

    @JsonView(FileBasicInfo.class)
    @Column(name="Type")
    private String type;

    @JsonView(FileBasicInfo.class)
    @Column(name="Content",columnDefinition="MEDIUMBLOB")
    private String content;

    @JsonView(FileExtendedInfo.class)
    @ManyToOne
    private Task task;

    @Column(name="write_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date write_date;

    @Column(name="create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
    public File() {
        this.create_date=new Date();
        this.write_date =new Date();
    }
    public File(String name, String content, String type,Task task) {
        this.name=name;
        this.content=content;
        this.type=type;
        this.create_date=new Date();
        this.task=task;
        this.write_date =new Date();
    }
}
