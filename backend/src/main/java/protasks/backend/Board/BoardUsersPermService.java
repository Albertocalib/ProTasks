package protasks.backend.Board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BoardUsersPermService {

    @Autowired
    BoardUsersPermRepository boardRepository;

    public void save(BoardUsersPermRel board){
        this.boardRepository.save(board);
    }
    public BoardUsersPermRel findBoardPermByUserIdAndBoardId(Long boardId,Long userId){ return this.boardRepository.findBoardPermByUserIdAndBoardId(boardId,userId);}

}

