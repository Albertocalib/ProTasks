package protasks.backend.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    public List<Board> findByUsername(String username){
        return this.boardRepository.findByUsername(username);
    }
}

