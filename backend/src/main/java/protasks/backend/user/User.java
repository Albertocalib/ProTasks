package protasks.backend.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Id")
    private int id;
    @Column(name="Name")
    private String name;
    @Column(name="Surname")
    private String surname;
    @Column(name="Username")
    private String username;
    @Column(name="Password")
    private String password;
    @Column(name="Email")
    private String email;

    public User() {
    }
    public User(String name,String surname,String userName,String password,String email ) {
        this.name = name;
        this.surname=surname;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = userName;
        this.email=email;
    }
    public int getId() {
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
}
