package protasks.backend.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import protasks.backend.Tag.Tag;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findById(long id);
}
