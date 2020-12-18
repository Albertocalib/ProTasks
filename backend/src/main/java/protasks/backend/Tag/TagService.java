package protasks.backend.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;


    public void save(Tag t) {
        this.tagRepository.save(t);
    }

    public Optional<Tag> findTagById(Long id) {
        return this.tagRepository.findById(id);
    }
    public List<Tag> findByBoardId(Long id){return this.tagRepository.findByBoardId(id);}
    
}

