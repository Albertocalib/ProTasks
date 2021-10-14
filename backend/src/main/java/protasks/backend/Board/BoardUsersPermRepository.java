package protasks.backend.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardUsersPermRepository extends JpaRepository<BoardUsersPermRel,Long> {

    @Query(value = "SELECT b.* FROM boards_users_rel b where b.user_id=:userId and b.board_id=:boardId",nativeQuery = true)
    BoardUsersPermRel findBoardPermByUserIdAndBoardId(@Param("boardId") Long boardId,@Param("userId") Long userId) ;

}
