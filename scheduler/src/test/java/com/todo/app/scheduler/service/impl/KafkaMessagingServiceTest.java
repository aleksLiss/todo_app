package com.todo.app.scheduler.service.impl;

import com.todo.app.scheduler.dto.KafkaMessageDto;
import com.todo.app.scheduler.model.Task;
import com.todo.app.scheduler.model.User;
import com.todo.app.scheduler.repository.TaskRepository;
import com.todo.app.scheduler.service.MessageCreator;
import com.todo.app.scheduler.service.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessagingServiceTest {

    @Mock
    private MessageSender messageSender;
    @Mock
    private MessageCreator messageCreator;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private KafkaMessagingService kafkaMessagingService;
    private User user;
    private User user2;
    private Task task;
    private Task task2;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("user1");
        user.setPassword("password1");
        user2 = new User();
        user2.setEmail("user2");
        user2.setPassword("password2");
        task = new Task();
        task.setTitle("title1");
        task.setUser(user);
        task.setCompleted(true);
        task2 = new Task();
        task2.setTitle("title2");
        task2.setUser(user2);
        task2.setCompleted(false);
    }

    @Test
    public void whenProcessMessageThenOk() {
        List<Task> tasks = List.of(task, task2);
        when(taskRepository.findAllByCreatedAtBetweenWithUser(any(), any()))
                .thenReturn(tasks);
        KafkaMessageDto msg1 = new KafkaMessageDto(user.getEmail(), "Title", "Body 1");
        KafkaMessageDto msg2 = new KafkaMessageDto(user2.getEmail(), "Title", "Body 2");
        when(messageCreator.create(eq(true), anyList(), eq(user))).thenReturn(msg1);
        when(messageCreator.create(eq(false), anyList(), eq(user2))).thenReturn(msg2);
        kafkaMessagingService.processMessage();
        verify(messageSender, times(2)).sendMessage(any(KafkaMessageDto.class));
        verify(messageSender).sendMessage(msg1);
        verify(messageSender).sendMessage(msg2);
    }

    @Test
    public void whenProcessMessageAndNotSavedTasksThenNotProcessMessage() {
        when(taskRepository.findAllByCreatedAtBetweenWithUser(any(), any()))
                .thenReturn(List.of());
        kafkaMessagingService.processMessage();
        verifyNoInteractions(messageCreator);
        verifyNoInteractions(messageSender);
    }
}