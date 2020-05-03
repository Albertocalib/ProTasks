package protasks.backend.Board;

import com.fasterxml.jackson.annotation.JsonView;
import protasks.backend.Rol.Rol;
import protasks.backend.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "boards_users_rel")
public class BoardUsersPermRel {
    public interface BoardBasicInfo{}
    public interface UserBasicInfo{}

    @JsonView(BoardUsersPermRel.class)
    @EmbeddedId
    private BoardUsersPermId id;

    @JsonView(UserBasicInfo.class)
    @ManyToOne
    @MapsId("boardId")
    private Board board;

    @JsonView(BoardBasicInfo.class)
    @ManyToOne
    @MapsId("userId")
    private User user;

    @JsonView(BoardUsersPermRel.class)
    @Column(name = "rol")
    private Rol rol ;

    public BoardUsersPermRel() {}

    public BoardUsersPermRel(Board board, User user,Rol rol) {
        this.board = board;
        this.user = user;
        this.rol=rol;
        this.id = new BoardUsersPermId(board.getId(), user.getId());
    }

    public BoardUsersPermId getId() {
        return id;
    }

    public void setId(BoardUsersPermId id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        BoardUsersPermRel that = (BoardUsersPermRel) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, user);
    }
}