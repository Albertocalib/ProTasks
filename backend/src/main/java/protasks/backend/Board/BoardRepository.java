package protasks.backend.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    @Query(value = "SELECT b.* FROM board b join boards_users_rel bur on bur.board_id=b.id join user u on bur.user_id=u.id WHERE LOWER(u.username) = LOWER(:username) or LOWER(u.email) = LOWER(:username)",nativeQuery = true)
    List<Board> findByUsername(@Param("username") String username);

    @Query(value = "SELECT b.* FROM board b join boards_users_rel bur on bur.board_id=b.id join user u on bur.user_id=u.id WHERE (LOWER(u.username) = LOWER(:username) or LOWER(u.email) = LOWER(:username)) and LOWER(b.name) like CONCAT(CONCAT('%',LOWER(:name)),'%')",nativeQuery = true)
    List<Board> filterBoardsByName(@Param("name") String name,@Param("username") String username);

    @Query(value = "SELECT b.* FROM board b join boards_users_rel bur on bur.board_id=b.id join user u on bur.user_id=u.id WHERE (LOWER(u.username) = LOWER(:username) or LOWER(u.email) = LOWER(:username)) and LOWER(b.name) like LOWER(:name)",nativeQuery = true)
    List<Board> filterBoardsByNameUnique(@Param("name") String name,@Param("username") String username);

    @Query(value = "SELECT b.* FROM board b " +
            "join task_list tl on b.id=tl.board_id " +
            "WHERE tl.id=:id",nativeQuery = true)
    Board findBoardByTaskListId(@Param("id") Long id);

}
