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
import protasks.backend.RestControllers.TagRestController;
import protasks.backend.Tag.Tag;
import protasks.backend.Tag.TagService;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
class TagRestControllerTests {

    @InjectMocks
    private TagRestController tagRestController;

    @Mock
    private TaskService taskService;

    @Mock
    private TagService tagService;

    @Mock
    private BoardService boardService;

    @Test
    void testGetTagsByTaskNoExistTask() {
        MockitoAnnotations.initMocks(this);
        long taskId = Integer.toUnsignedLong(6);
        Mockito.when(taskService.findById(taskId)).thenReturn(null);
        ResponseEntity<List<Tag>> response = tagRestController.getTagsByTaskId(taskId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetTagsByTaskExistTask() {
        MockitoAnnotations.initMocks(this);
        long taskId = Integer.toUnsignedLong(6);
        Task t=new Task();
        t.setTitle("Prueba");
        Tag tag=new Tag();
        tag.setName("Tag1");
        Tag tag2=new Tag();
        tag2.setName("Tag1");
        t.addTag(tag);
        t.addTag(tag2);
        Mockito.when(taskService.findById(taskId)).thenReturn(t);
        ResponseEntity<List<Tag>> response = tagRestController.getTagsByTaskId(taskId);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(t.getTag_ids(),response.getBody());

    }
    @Test
    void testDeleteTagNoExistTag() {
        MockitoAnnotations.initMocks(this);
        long tagId = Integer.toUnsignedLong(6);
        long taskId = Integer.toUnsignedLong(2);
        Mockito.when(tagService.findTagById(tagId)).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = tagRestController.deleteUserToTask(tagId,taskId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }
    @Test
    void testDeleteTagNoExistTask() {
        MockitoAnnotations.initMocks(this);
        long tagId = Integer.toUnsignedLong(6);
        long taskId = Integer.toUnsignedLong(2);
        Tag t=new Tag();
        t.setName("P");
        Mockito.when(tagService.findTagById(tagId)).thenReturn(Optional.of(t));
        Mockito.when(taskService.findById(taskId)).thenReturn(null);
        ResponseEntity<Boolean> response = tagRestController.deleteUserToTask(tagId,taskId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }

    @Test
    void testDeleteTagOK() {
        MockitoAnnotations.initMocks(this);
        long tagId = Integer.toUnsignedLong(6);
        long taskId = Integer.toUnsignedLong(2);
        Tag t=new Tag();
        t.setName("P");
        Task task=new Task();
        task.addTag(t);
        Mockito.when(tagService.findTagById(tagId)).thenReturn(Optional.of(t));
        Mockito.when(taskService.findById(taskId)).thenReturn(task);
        ResponseEntity<Boolean> response = tagRestController.deleteUserToTask(tagId,taskId);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(true,response.getBody());
    }

    @Test
    void testGetTagsInBoardNoTags() {
        MockitoAnnotations.initMocks(this);
        long boardId = Integer.toUnsignedLong(6);
        Mockito.when(tagService.findByBoardId(boardId)).thenReturn(null);
        ResponseEntity<List<Tag>> response = tagRestController.getTagsInBoard(boardId);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testGetTagsInBoardOK() {
        MockitoAnnotations.initMocks(this);
        long boardId = Integer.toUnsignedLong(6);
        Tag t=new Tag();
        t.setName("P");
        Tag t2=new Tag();
        t2.setName("P2");
        Board b= new Board();
        ArrayList<Tag> list= new ArrayList<>();
        list.add(t);
        list.add(t2);
        b.setTags(list);
        Mockito.when(tagService.findByBoardId(boardId)).thenReturn(list);
        ResponseEntity<List<Tag>> response = tagRestController.getTagsInBoard(boardId);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertEquals(list,response.getBody());
    }

    @Test
    void testAddTagToTaskNoTask() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(taskService.findById(5)).thenReturn(null);
        long taskId=5;
        long tagId=2;
        ResponseEntity<Boolean> response = tagRestController.addTagToTask(taskId,tagId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }

    @Test
    void testAddTagToTaskNoTag() {
        MockitoAnnotations.initMocks(this);
        long taskId=5;
        long tagId=2;
        Mockito.when(taskService.findById(taskId)).thenReturn(new Task());
        Mockito.when(tagService.findTagById(tagId)).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = tagRestController.addTagToTask(taskId,tagId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),response.getStatusCodeValue());
    }
    @Test
    void testAddTagToTaskOk() {
        MockitoAnnotations.initMocks(this);
        long taskId=5;
        long tagId=2;
        Task t=new Task();
        Tag tag =new Tag();
        Mockito.when(taskService.findById(taskId)).thenReturn(t);
        Mockito.when(tagService.findTagById(tagId)).thenReturn(Optional.of(tag));
        ResponseEntity<Boolean> response = tagRestController.addTagToTask(taskId,tagId);
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assertions.assertTrue(t.getTag_ids().contains(tag));
    }

    @Test
    void testCreateTagAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<Tag> response = tagRestController.createTag(null,"","");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = tagRestController.createTag(new Tag(),null,"");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
        response = tagRestController.createTag(new Tag(),"",null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testCreateTagBoardNull() {
        MockitoAnnotations.initMocks(this);
        String boardName="boardName";
        String user="user";
        Mockito.when(boardService.filterBoardsByNameUnique(boardName,user)).thenReturn(null);
        ResponseEntity<Tag> response = tagRestController.createTag(new Tag(),boardName,user);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }
    @Test
    void testCreateTagMoreThanOneBoard() {
        MockitoAnnotations.initMocks(this);
        Board b1= new Board();
        Board b2= new Board();
        ArrayList<Board> boards = new ArrayList<>();
        boards.add(b1);
        boards.add(b2);
        String boardName="boardName";
        String user="user";
        Mockito.when(boardService.filterBoardsByNameUnique(boardName,user)).thenReturn(boards);
        ResponseEntity<Tag> response = tagRestController.createTag(new Tag(),boardName,user);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }
    @Test
    void testCreateTagOk() {
        MockitoAnnotations.initMocks(this);
        Board b1= new Board();
        ArrayList<Board> boards = new ArrayList<>();
        boards.add(b1);
        String boardName="boardName";
        String user="user";
        Tag t= new Tag();
        Mockito.when(boardService.filterBoardsByNameUnique(boardName,user)).thenReturn(boards);
        ResponseEntity<Tag> response = tagRestController.createTag(t,boardName,user);
        Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());
        Assertions.assertEquals(b1, Objects.requireNonNull(response.getBody()).getBoard());
    }
}
