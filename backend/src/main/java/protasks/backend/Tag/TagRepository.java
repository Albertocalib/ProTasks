package protasks.backend.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import protasks.backend.Board.Board;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findById(long id);
    List<Tag> findByBoardId(long id);
}
