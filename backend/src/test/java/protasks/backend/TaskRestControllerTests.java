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
import protasks.backend.File.File;
import protasks.backend.File.FileService;
import protasks.backend.RestControllers.TaskRestController;
import protasks.backend.Rol.Priority;
import protasks.backend.Task.Task;
import protasks.backend.Task.TaskService;
import protasks.backend.TaskList.TaskList;
import protasks.backend.TaskList.TaskListService;
import protasks.backend.user.User;
import protasks.backend.user.UserService;

import java.util.*;

@SpringBootTest
class TaskRestControllerTests {

    @InjectMocks
    private TaskRestController taskRestController;

    @Mock
    TaskListService listService;

    @Mock
    TaskService taskService;

    @Mock
    UserService userService;

    @Mock
    FileService fileService;


    @Test
    void testCreateTaskAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<Task> response = taskRestController.createTask(null, "", "", "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.createTask(new Task(), null, "", "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.createTask(new Task(), "", null, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.createTask(new Task(), "", "", null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testCreateTaskOKUpdateTime() {
        MockitoAnnotations.initMocks(this);
        Board b = new Board();
        b.setName("Prueba1");
        b.setTimeActivated(true);
        b.setCycleStartList("TO DO");
        TaskList tl = new TaskList();
        tl.setTitle("TO DO");
        TaskList tl2 = new TaskList();
        tl2.setTitle("DOING");
        tl2.setBoard(b);
        tl.setBoard(b);
        ArrayList<TaskList> list = new ArrayList<>();
        list.add(tl);
        Mockito.when(listService.findTaskList("Albertocalib", "Prueba1", "To DO")).thenReturn(list);
        Task t = new Task();
        t.setTaskList(tl);
        t.setTitle("Prueba");
        ResponseEntity<Task> response = taskRestController.createTask(t, "Prueba1", "To DO", "Albertocalib");
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t, response.getBody());
        Assertions.assertNotNull(t.getDate_start_cycle_time());
    }

    @Test
    void testCreateTaskOKNoUpdateTime() {
        MockitoAnnotations.initMocks(this);
        Board b = new Board();
        b.setName("Prueba1");
        b.setTimeActivated(false);
        b.setCycleStartList("TO DO");
        TaskList tl = new TaskList();
        tl.setTitle("TO DO");
        TaskList tl2 = new TaskList();
        tl2.setTitle("DOING");
        tl2.setBoard(b);
        tl.setBoard(b);
        ArrayList<TaskList> list = new ArrayList<>();
        list.add(tl);
        Mockito.when(listService.findTaskList("Albertocalib", "Prueba1", "To DO")).thenReturn(list);
        Task t = new Task();
        t.setTaskList(tl);
        t.setTitle("Prueba");
        ResponseEntity<Task> response = taskRestController.createTask(t, "Prueba1", "To DO", "Albertocalib");
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t, response.getBody());
        Assertions.assertNull(t.getDate_start_cycle_time());
    }

    @Test
    void testCreateTaskMoreThanOneList() {
        MockitoAnnotations.initMocks(this);
        Board b = new Board();
        b.setName("Prueba1");
        TaskList tl = new TaskList();
        tl.setTitle("TO DO");
        TaskList tl2 = new TaskList();
        tl2.setTitle("TO DO");
        tl2.setBoard(b);
        tl.setBoard(b);
        ArrayList<TaskList> list = new ArrayList<>();
        list.add(tl);
        list.add(tl2);
        b.setTaskLists(list);
        Mockito.when(listService.findTaskList("Albertocalib", "Prueba1", "To DO")).thenReturn(b.getTaskLists());
        ResponseEntity<Task> response = taskRestController.createTask(new Task(), "Prueba1", "To DO", "Albertocalib");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testCreateTaskNoList() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(listService.findTaskList("Albertocalib", "Prueba1", "To DO")).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.createTask(new Task(), "Prueba1", "To DO", "Albertocalib");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testGetTasksByUsernameNoTasks() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(taskService.findByUsername("Albertocalib12")).thenReturn(null);
        ResponseEntity<List<Task>> response = taskRestController.getTasksByUsername("Albertocalib12");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testGetTasksByUsernameOk() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        List<Task> tasks = new ArrayList<>();
        tasks.add(t);
        Mockito.when(taskService.findByUsername("Albertocalib")).thenReturn(tasks);
        ResponseEntity<List<Task>> response = taskRestController.getTasksByUsername("Albertocalib");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void testGetTasksFilterByNameNoTasks() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(taskService.filterByName("Prueba1", "Albertocalib12")).thenReturn(null);
        ResponseEntity<List<Task>> response = taskRestController.getTasksFilterByName("Prueba1", "Albertocalib12");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testGetTasksFilterByNameOk() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        List<Task> tasks = new ArrayList<>();
        tasks.add(t);
        Mockito.when(taskService.filterByName("Prueba1", "Albertocalib12")).thenReturn(tasks);
        ResponseEntity<List<Task>> response = taskRestController.getTasksFilterByName("Prueba1", "Albertocalib12");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateTaskPositionAnyParameterNull() {
        MockitoAnnotations.initMocks(this);
        long newPosition = 2;
        long newList = 3;
        long id = 1;
        ResponseEntity<Task> response = taskRestController.updateTaskPosition(null, newPosition, newList);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

        response = taskRestController.updateTaskPosition(id, null, newList);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

        response = taskRestController.updateTaskPosition(id, newPosition, null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateTaskPositionNoTask() {
        MockitoAnnotations.initMocks(this);
        long newPosition = 2;
        long newList = 3;
        long id = 1;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.updateTaskPosition(id, newPosition, newList);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateTaskPositionSameTaskListOk() {
        MockitoAnnotations.initMocks(this);
        long newPosition = 3;
        long newList = 3;
        long id = 1;
        Task t = new Task();
        t.setId(1);
        t.setPosition(1);
        Task t2 = new Task();
        t2.setPosition(2);
        Task t3 = new Task();
        t3.setPosition(3);
        TaskList tl = new TaskList();
        tl.setId(newList);
        t.setTaskList(tl);
        t2.setTaskList(tl);
        t3.setTaskList(tl);
        tl.addTask(t);
        tl.addTask(t2);
        tl.addTask(t3);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.updateTaskPosition(id, newPosition, newList);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(3, t.getPosition());
        Assertions.assertEquals(2, t3.getPosition());
        Assertions.assertEquals(1, t2.getPosition());
        Assertions.assertEquals(t, response.getBody());

    }

    @Test
    void testUpdateTaskPositionOtherTaskListOk() {
        MockitoAnnotations.initMocks(this);
        long newPosition = 2;
        long newList = 3;
        long id = 1;
        Task t = new Task();
        t.setId(1);
        t.setPosition(1);
        Task t2 = new Task();
        t2.setPosition(2);
        Task t3 = new Task();
        t3.setPosition(3);
        TaskList tl = new TaskList();
        TaskList tl2 = new TaskList();
        tl.setId(1);
        tl2.setId(newList);
        t.setTaskList(tl);
        t2.setTaskList(tl);
        t3.setTaskList(tl);
        tl.addTask(t);
        tl.addTask(t2);
        tl.addTask(t3);
        Task t4 = new Task();
        t4.setPosition(0);
        t4.setTaskList(tl2);
        tl2.addTask(t4);
        Board b = new Board();
        b.setTimeActivated(false);
        tl2.setBoard(b);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        Mockito.when(listService.findById(newList)).thenReturn(tl2);
        ResponseEntity<Task> response = taskRestController.updateTaskPosition(id, newPosition, newList);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(2, t.getPosition());
        Assertions.assertEquals(2, t3.getPosition());
        Assertions.assertEquals(1, t2.getPosition());
        Assertions.assertEquals(t, response.getBody());
        Assertions.assertEquals(tl2, Objects.requireNonNull(response.getBody()).getTaskList());

    }

    @Test
    void testGetUsersByTaskIdNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<List<User>> response = taskRestController.getUsersByTaskId(id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testGetUsersByTaskIdOk() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<List<User>> response = taskRestController.getUsersByTaskId(id);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateTitleTaskNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.updateTitleTask(id, "");
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateTitleTaskOk() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        t.setTitle("Prueba");
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.updateTitleTask(id, "Prueba2");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t.getTitle(), "Prueba2");
    }

    @Test
    void testUpdatePriorityNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.updatePriorityTask(id, Priority.NORMAL);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdatePriorityOk() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.updatePriorityTask(id, Priority.NORMAL);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t.getPriority(), Priority.NORMAL);
    }

    @Test
    void testUpdateDateEndNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.updateDateEnd(id, new Date());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateDateEndOk() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(t);
        Date date = new Date();
        ResponseEntity<Task> response = taskRestController.updateDateEnd(id, date);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t.getDate_end(), date);
    }

    @Test
    void testUpdateDescriptionTaskNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.updateDescriptionTask(id, "");
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testUpdateDescriptionTaskOK() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.updateDescriptionTask(id, "date");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t.getDescription(), "date");
    }

    @Test
    void testAddUserToTaskNoUser() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(userService.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = taskRestController.addUserToTask(id, id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testAddUserToTaskNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        User u = new User();
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(u));
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Boolean> response = taskRestController.addUserToTask(id, id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testAddUserToTaskOK() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        User u = new User();
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(u));
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Boolean> response = taskRestController.addUserToTask(id, id);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(Boolean.TRUE, response.getBody());
        Assertions.assertTrue(u.getTasks().contains(t));
    }

    @Test
    void testDeleteUserToTaskNoUser() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(userService.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Boolean> response = taskRestController.deleteUserToTask(id, id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testDeleteUserToTaskNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        User u = new User();
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(u));
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Boolean> response = taskRestController.deleteUserToTask(id, id);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    void testDeleteUserToTaskOK() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        User u = new User();
        u.addTask(t);
        Mockito.when(userService.findById(id)).thenReturn(Optional.of(u));
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Boolean> response = taskRestController.deleteUserToTask(id, id);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(Boolean.TRUE, response.getBody());
        Assertions.assertFalse(u.getTasks().contains(t));
    }

    @Test
    void testAddAttachmentsNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.addAttachments(id, new HashSet<>());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testAddAttachmentsOK() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.addAttachments(id, new HashSet<>());
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t, response.getBody());
    }

    @Test
    void testAddSubtasksNoTask() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.addSubtasks(id, new Task());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testAddSubTasksOK() {
        MockitoAnnotations.initMocks(this);
        Task t = new Task();
        long id = 2;
        Task parent_t = new Task();
        t.setParent_task(parent_t);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.addSubtasks(id, parent_t);
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t, response.getBody());
        Assertions.assertEquals(parent_t, Objects.requireNonNull(response.getBody()).getParent_task());

    }

    @Test
    void testRemoveSubtasksNoTask() {
        MockitoAnnotations.initMocks(this);
        String ids = "2";
        long id1 = 2;
        Mockito.when(taskService.findById(id1)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.removeSubtasks(ids);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testRemoveSubtasksNoAllTask() {
        MockitoAnnotations.initMocks(this);
        String ids = "2&3";
        long id1 = 2;
        long id2 = 3;
        Mockito.when(taskService.findById(id1)).thenReturn(new Task());
        Mockito.when(taskService.findById(id2)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.removeSubtasks(ids);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testRemoveSubtasksNoIds() {
        MockitoAnnotations.initMocks(this);
        String ids = "";
        ResponseEntity<Task> response = taskRestController.removeSubtasks(ids);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testRemoveSubTasksOK() {
        MockitoAnnotations.initMocks(this);
        String ids = "2&3";
        long id1 = 2;
        long id2 = 3;
        Task t = new Task();
        Task t1 = new Task();
        Task p = new Task();
        t1.setParent_task(p);
        Mockito.when(taskService.findById(id1)).thenReturn(t);
        Mockito.when(taskService.findById(id2)).thenReturn(t1);
        ResponseEntity<Task> response = taskRestController.removeSubtasks(ids);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(p, response.getBody());
        Mockito.verify(taskService, Mockito.times(1)).delete(t);
        Mockito.verify(taskService, Mockito.times(1)).delete(t);


    }

    @Test
    void testRemoveAttachmentNoFile() {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(fileService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.removeAttachment(id, id);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testRemoveAttachmentOK() {
        MockitoAnnotations.initMocks(this);
        File f = new File();
        long id = 2;
        Task t = new Task();
        List<File> list = new ArrayList<>();
        list.add(f);
        t.setAttachments(list);
        Mockito.when(fileService.findById(id)).thenReturn(f);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.removeAttachment(id, id);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(t, response.getBody());
        Mockito.verify(fileService, Mockito.times(1)).delete(f);

    }

    @Test
    void testMoveTaskAnyParameterNull() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long taskIdOrBoardId = 2;
        ResponseEntity<Task> response = taskRestController.moveTask(null, "", taskIdOrBoardId, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.moveTask(taskIdOrBoardId, null, taskIdOrBoardId, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.moveTask(taskIdOrBoardId, "", null, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.moveTask(taskIdOrBoardId, "", taskIdOrBoardId, null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testMoveTaskNoTask() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.moveTask(id, "", id, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testMoveTaskSameList() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Task t = new Task();
        TaskList tl = new TaskList();
        tl.setTitle("Prueba");
        tl.addTask(t);
        t.setTaskList(tl);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        ResponseEntity<Task> response = taskRestController.moveTask(id, "Prueba", id, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testMoveTaskNoTaskListOrMoreThanOne() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Task t = new Task();
        TaskList tl = new TaskList();
        TaskList tl2 = new TaskList();
        tl.setTitle("Prueba");
        tl.addTask(t);
        t.setTaskList(tl);
        List<TaskList> list = new ArrayList<>();
        list.add(tl);
        list.add(tl2);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        Mockito.when(listService.findTaskList(id, "Prueba1")).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.moveTask(id, "Prueba1", id, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

        Mockito.when(listService.findTaskList(id, "Prueba")).thenReturn(list);
        response = taskRestController.moveTask(id, "Prueba", id, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testMoveTaskOk() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Task t = new Task();
        t.setPosition(2);
        Task t_old1 = new Task();
        t_old1.setPosition(1);
        Task t_old2 = new Task();
        t_old2.setPosition(3);
        Task t_old3 = new Task();
        t_old3.setPosition(4);
        Task t_new1 = new Task();
        t_new1.setPosition(1);
        TaskList tl = new TaskList();
        TaskList tl2 = new TaskList();
        tl.setTitle("Prueba");
        tl2.addTask(t);
        tl.addTask(t_new1);
        tl2.addTask(t_old1);
        tl2.addTask(t_old2);
        tl2.addTask(t_old3);
        t.setTaskList(tl2);
        t_old1.setTaskList(tl2);
        t_old2.setTaskList(tl2);
        t_old3.setTaskList(tl2);
        t_new1.setTaskList(tl);
        List<TaskList> list = new ArrayList<>();
        list.add(tl);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        Mockito.when(listService.findTaskList(id, "Prueba1")).thenReturn(list);
        ResponseEntity<Task> response = taskRestController.moveTask(id, "Prueba1", id, "");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        Assertions.assertEquals(2, t.getPosition());
        Assertions.assertEquals(1, t_old1.getPosition());
        Assertions.assertEquals(2, t_old2.getPosition());
        Assertions.assertEquals(3, t_old3.getPosition());
        Assertions.assertTrue(tl.getTasks().contains(t));
        Assertions.assertFalse(tl2.getTasks().contains(t));

    }

    @Test
    void testCopyTaskAnyParameterNull() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long taskIdOrBoardId = 2;
        ResponseEntity<Task> response = taskRestController.copyTask(null, "", taskIdOrBoardId, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.copyTask(taskIdOrBoardId, null, taskIdOrBoardId, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.copyTask(taskIdOrBoardId, "", null, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        response = taskRestController.copyTask(taskIdOrBoardId, "", taskIdOrBoardId, null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testCopyTaskNoTask() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Mockito.when(taskService.findById(id)).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.copyTask(id, "", id, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }


    @Test
    void testCopyTaskNoTaskListOrMoreThanOne() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Task t = new Task();
        TaskList tl = new TaskList();
        TaskList tl2 = new TaskList();
        tl.setTitle("Prueba");
        tl.addTask(t);
        t.setTaskList(tl);
        List<TaskList> list = new ArrayList<>();
        list.add(tl);
        list.add(tl2);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        Mockito.when(listService.findTaskList(id, "Prueba1")).thenReturn(null);
        ResponseEntity<Task> response = taskRestController.copyTask(id, "Prueba1", id, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

        Mockito.when(listService.findTaskList(id, "Prueba")).thenReturn(list);
        response = taskRestController.copyTask(id, "Prueba", id, "");
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    void testCopyTaskOk() throws CloneNotSupportedException {
        MockitoAnnotations.initMocks(this);
        long id = 2;
        Task t = new Task();
        t.setPosition(2);
        Task t_old1 = new Task();
        t_old1.setPosition(1);
        Task t_old2 = new Task();
        t_old2.setPosition(3);
        Task t_old3 = new Task();
        t_old3.setPosition(4);
        Task t_new1 = new Task();
        t_new1.setPosition(1);
        TaskList tl = new TaskList();
        TaskList tl2 = new TaskList();
        tl.setTitle("Prueba");
        tl2.addTask(t);
        tl.addTask(t_new1);
        tl2.addTask(t_old1);
        tl2.addTask(t_old2);
        tl2.addTask(t_old3);
        t.setTaskList(tl2);
        t_old1.setTaskList(tl2);
        t_old2.setTaskList(tl2);
        t_old3.setTaskList(tl2);
        t_new1.setTaskList(tl);
        List<TaskList> list = new ArrayList<>();
        list.add(tl);
        Mockito.when(taskService.findById(id)).thenReturn(t);
        Mockito.when(listService.findTaskList(id, "Prueba1")).thenReturn(list);
        ResponseEntity<Task> response = taskRestController.copyTask(id, "Prueba1", id, "");
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

}
