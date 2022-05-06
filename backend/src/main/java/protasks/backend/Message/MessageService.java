package protasks.backend.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import protasks.backend.Tag.Tag;

import java.util.List;
import java.util.Optional;


@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;


    public void save(Message t) {
        this.messageRepository.save(t);
    }

    public Optional<Message> findById(Long id) {
        return this.messageRepository.findById(id);
    }

}

