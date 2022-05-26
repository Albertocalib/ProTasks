package protasks.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import protasks.backend.Message.Message;
import protasks.backend.Message.MessageService;
import protasks.backend.RestControllers.MessageRestController;

@SpringBootTest
class MessageRestControllerTests {

    @InjectMocks
    private MessageRestController messageRestController;

    @Mock
    private MessageService messageService;

    @Test
    void testNewMessageNull() {
        MockitoAnnotations.initMocks(this);
        ResponseEntity<Message> response = messageRestController.newMessage(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatusCodeValue());
    }

    @Test
    void testNewMessageOk() {
        MockitoAnnotations.initMocks(this);
        Message m = new Message();
        ResponseEntity<Message> response = messageRestController.newMessage(m);
        Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());
        Assertions.assertEquals(m,response.getBody());
    }



}
