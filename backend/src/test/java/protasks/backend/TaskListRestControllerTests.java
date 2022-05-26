package protasks.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import protasks.backend.Board.Board;
import protasks.backend.Board.BoardService;
import protasks.backend.RestControllers.TaskListRestController;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class TaskListRestControllerTests {
    @InjectMocks
    private TaskListRestController taskListRestController;

    @Mock
    private TaskListService listService;

    @Mock
    private BoardService boardService;

    @Test
    void testCreateTaskListAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<TaskList> response = taskListRestController.createTaskList(null,"","");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.createTaskList(new TaskList(),null,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.createTaskList(new TaskList(),"",null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testCreateTaskListMoreThanOneBoard() {
        MockitoAnnotations.initMocks(this);
        Board b=new Board();
        Board b1=new Board();
        List<Board> boards = new ArrayList<>();
        boards.add(b);
        boards.add(b1);
        Mockito.when(boardService.filterBoardsByNameUnique("PR1","ACL")).thenReturn(boards);
        ResponseEntity<TaskList> response = taskListRestController.createTaskList(new TaskList(),"PR1","ACL");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testCreateTaskListMoreNoBoard() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(boardService.filterBoardsByNameUnique("PR1","ACL")).thenReturn(null);
        ResponseEntity<TaskList> response = taskListRestController.createTaskList(new TaskList(),"PR1","ACL");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testCreateTaskListOk() {
        MockitoAnnotations.initMocks(this);
        Board b=new Board();
        List<Board> boards = new ArrayList<>();
        boards.add(b);
        Mockito.when(boardService.filterBoardsByNameUnique("PR1","ACL")).thenReturn(boards);
        TaskList list = new TaskList();
        ResponseEntity<TaskList> response = taskListRestController.createTaskList(list,"PR1","ACL");
        Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());
        Assertions.assertEquals(list,response.getBody());
    }

    @Test
    void testGetTaskListsByBoardAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<List<TaskList>> response = taskListRestController.getTaskListsByBoard(null,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.getTaskListsByBoard("",null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetTaskListsByBoardOK() {
        MockitoAnnotations.initMocks(this);
        TaskList tl= new TaskList();
        TaskList tl2 = new TaskList();
        List<TaskList> list = new ArrayList<>();
        list.add(tl);
        list.add(tl2);
        Mockito.when(listService.findTasksListsByBoardName("PR1","ACL")).thenReturn(list);
        ResponseEntity<List<TaskList>> response = taskListRestController.getTaskListsByBoard("","");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetTaskListsByBoardNoBoards() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(listService.findTasksListsByBoardName("PR1","ACL")).thenReturn(null);
        ResponseEntity<List<TaskList>> response = taskListRestController.getTaskListsByBoard("","");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    void testUpdatePositionAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        long p=2;
        ResponseEntity<TaskList> response = taskListRestController.updatePosition(null,p);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.updatePosition(p,null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testUpdatePositionOk() {
        MockitoAnnotations.initMocks(this);
        Board b = new Board();
        TaskList tl2= new TaskList();
        tl2.setPosition(3);
        TaskList tl3= new TaskList();
        tl3.setPosition(2);
        long p=3;
        long id=2;
        TaskList t= new TaskList();
        t.setPosition(1);
        ArrayList<TaskList> tl= new ArrayList<>();
        tl.add(tl2);
        tl.add(tl3);
        tl.add(t);
        b.setTaskLists(tl);
        tl2.setBoard(b);
        tl3.setBoard(b);
        t.setBoard(b);
        Mockito.when(listService.findById(id)).thenReturn(t);
        ResponseEntity<TaskList> response = taskListRestController.updatePosition(id,p);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(3,t.getPosition());
        Assertions.assertEquals(2,tl2.getPosition());
        Assertions.assertEquals(1,tl3.getPosition());
        response = taskListRestController.updatePosition(id,p-1);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(2,t.getPosition());
        Assertions.assertEquals(3,tl2.getPosition());
        Assertions.assertEquals(1,tl3.getPosition());
    }

    @Test
    void testUpdatePositionNoTaskList() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(listService.findById(id)).thenReturn(null);
        ResponseEntity<TaskList> response = taskListRestController.updatePosition(id,id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }
    @Test
    void testGetTaskBoardListsByListIdIdNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<List<TaskList>> response = taskListRestController.getTaskBoardListsByListId(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetTaskBoardListsByListIdNoBoard() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Mockito.when(boardService.findBoardByTaskListId(id)).thenReturn(null);
        ResponseEntity<List<TaskList>> response = taskListRestController.getTaskBoardListsByListId(id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetTaskBoardListsByListIdOk() {
        MockitoAnnotations.initMocks(this);
        long id=2;
        Board b = new Board();
        Mockito.when(boardService.findBoardByTaskListId(id)).thenReturn(b);
        ResponseEntity<List<TaskList>> response = taskListRestController.getTaskBoardListsByListId(id);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(b.getTaskLists(),response.getBody());
    }

    @Test
    void testDeleteTaskListAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<Boolean> response = taskListRestController.deleteTaskList(null,"","");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.deleteTaskList("",null,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.deleteTaskList("","",null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void testDeleteTaskListOk() {
        MockitoAnnotations.initMocks(this);
        Board b = new Board();
        TaskList tl2= new TaskList();
        tl2.setPosition(3);
        TaskList tl3= new TaskList();
        tl3.setPosition(2);
        TaskList t= new TaskList();
        t.setPosition(1);
        ArrayList<TaskList> tl= new ArrayList<>();
        tl.add(tl2);
        tl.add(tl3);
        tl.add(t);
        b.setTaskLists(tl);
        tl2.setBoard(b);
        tl3.setBoard(b);
        t.setBoard(b);
        ArrayList<TaskList> tl_f= new ArrayList<>();
        tl_f.add(t);
        Mockito.when(listService.findTaskList("username", "boardName", "list")).thenReturn(tl_f);
        ResponseEntity<Boolean> response = taskListRestController.deleteTaskList("boardName", "list", "username");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,response.getBody());
        Assertions.assertEquals(2,tl2.getPosition());
        Assertions.assertEquals(1,tl3.getPosition());
    }

    @Test
    void testDeleteTaskListNoTaskList() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(listService.findTaskList("", "","")).thenReturn(null);
        ResponseEntity<Boolean> response = taskListRestController.deleteTaskList("","","");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testDeleteTaskListMoreThanOneTaskList() {
        MockitoAnnotations.initMocks(this);
        String username="ACL";
        String boardName="BOARD";
        String list="LIST";
        TaskList tl = new TaskList();
        List<TaskList> listTl= new ArrayList<>();
        listTl.add(tl);
        TaskList tl2 = new TaskList();
        listTl.add(tl2);
        Mockito.when(listService.findTaskList(username, boardName, list)).thenReturn(listTl);
        ResponseEntity<Boolean> response = taskListRestController.deleteTaskList(boardName,list,username);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testMoveTaskListAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<Boolean> response = taskListRestController.moveTaskList(null,"",(long) 0,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.moveTaskList("",null,(long) 0,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.moveTaskList("","",null,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.moveTaskList("","",(long) 0,null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void testMoveTaskListNoBoard() {
        MockitoAnnotations.initMocks(this);
        long boardId = 2;
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = taskListRestController.moveTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }
    @Test
    void testMoveTaskListNoTaskList() {
        MockitoAnnotations.initMocks(this);
        long boardId = 2;
        Board b = new Board();
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.of(b));
        Mockito.when(listService.findTaskList("","","")).thenReturn(null);
        ResponseEntity<Boolean> response = taskListRestController.moveTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void testMoveTaskListMoreThanOneTaskList() {
        MockitoAnnotations.initMocks(this);
        long boardId = 2;
        Board b = new Board();
        TaskList tl = new TaskList();
        TaskList tl2 = new TaskList();
        List<TaskList> list = new ArrayList<>();
        list.add(tl);
        list.add(tl2);
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.of(b));
        Mockito.when(listService.findTaskList("","","")).thenReturn(list);
        ResponseEntity<Boolean> response = taskListRestController.moveTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void testMoveTaskListOk() {
        MockitoAnnotations.initMocks(this);
        Board b = new Board();
        Board new_board = new Board();
        TaskList tl2= new TaskList();
        tl2.setPosition(3);
        TaskList tl3= new TaskList();
        tl3.setPosition(2);
        TaskList t= new TaskList();
        t.setPosition(1);
        ArrayList<TaskList> tl= new ArrayList<>();
        tl.add(tl2);
        tl.add(tl3);
        tl.add(t);
        b.setTaskLists(tl);
        tl2.setBoard(b);
        tl3.setBoard(b);
        t.setBoard(b);
        ArrayList<TaskList> tl_f= new ArrayList<>();
        tl_f.add(t);
        long boardId = 2;
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.of(new_board));
        Mockito.when(listService.findTaskList("","","")).thenReturn(tl_f);
        ResponseEntity<Boolean> response = taskListRestController.moveTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,response.getBody());
        Assertions.assertEquals(2,tl2.getPosition());
        Assertions.assertEquals(1,tl3.getPosition());
        Assertions.assertNotEquals(b,t.getBoard());
    }

    @Test
    void testCopyTaskListAnyParameterNull() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<Boolean> response = taskListRestController.copyTaskList(null,"",(long) 0,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.copyTaskList("",null,(long) 0,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.copyTaskList("","",null,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = taskListRestController.copyTaskList("","",(long) 0,null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void testCopyTaskListNoBoard() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long boardId = 2;
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = taskListRestController.copyTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }
    @Test
    void testCopyTaskListNoTaskList() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long boardId = 2;
        Board b = new Board();
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.of(b));
        Mockito.when(listService.findTaskList("","","")).thenReturn(null);
        ResponseEntity<Boolean> response = taskListRestController.copyTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void testCopyTaskListMoreThanOneTaskList() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long boardId = 2;
        Board b = new Board();
        TaskList tl = new TaskList();
        TaskList tl2 = new TaskList();
        List<TaskList> list = new ArrayList<>();
        list.add(tl);
        list.add(tl2);
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.of(b));
        Mockito.when(listService.findTaskList("","","")).thenReturn(list);
        ResponseEntity<Boolean> response = taskListRestController.copyTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());

    }

    @Test
    void testCopyTaskListOk() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        Board b = new Board();
        Board new_board = new Board();
        TaskList tl2= new TaskList();
        tl2.setPosition(3);
        TaskList tl3= new TaskList();
        tl3.setPosition(2);
        TaskList t= new TaskList();
        t.setPosition(1);
        ArrayList<TaskList> tl= new ArrayList<>();
        tl.add(tl2);
        tl.add(tl3);
        tl.add(t);
        b.setTaskLists(tl);
        tl2.setBoard(b);
        tl3.setBoard(b);
        t.setBoard(b);
        ArrayList<TaskList> tl_f= new ArrayList<>();
        tl_f.add(t);
        long boardId = 2;
        Mockito.when(boardService.findById(boardId)).thenReturn(Optional.of(new_board));
        Mockito.when(listService.findTaskList("","","")).thenReturn(tl_f);
        ResponseEntity<Boolean> response = taskListRestController.copyTaskList("","",boardId,"");
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,response.getBody());
    }

}
