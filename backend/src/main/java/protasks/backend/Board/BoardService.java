package protasks.backend.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import protasks.backend.File.File;

import java.util.List;
import java.util.Optional;


@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    public List<Board> filterBoardsByNameUnique(String name,String username){return this.boardRepository.filterBoardsByNameUnique(name,username);}
    public List<Board> filterByName(String name,String username){return this.boardRepository.filterBoardsByName(name,username);}
    public List<Board> findByUsername(String username){
        return this.boardRepository.findByUsername(username);
    }
    public void save(Board board){
        this.boardRepository.save(board);
    }
    public Board findBoardByTaskListId(Long id){
        return this.boardRepository.findBoardByTaskListId(id);
    }
    public Optional<Board> findById(Long id){ return this.boardRepository.findById(id);}
    public void delete(Board b) {this.boardRepository.delete(b); }

}

