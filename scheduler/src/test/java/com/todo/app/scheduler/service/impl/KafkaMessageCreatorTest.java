package com.todo.app.scheduler.service.impl;

import com.todo.app.scheduler.dto.KafkaMessageDto;
import com.todo.app.scheduler.model.Task;
import com.todo.app.scheduler.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KafkaMessageCreatorTest {

    private User user;
    private Task task;
    private Task task2;
    private List<Task> completedTasks;
    private List<Task> incompletedTasks;
    @InjectMocks
    private KafkaMessageCreator kafkaMessageCreator;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("email");
        user.setPassword("password");

        task = new Task();
        task.setTitle("task 1");
        task.setDescription("description task 1");
        task.setUser(user);
        task.setCompleted(false);

        task2 = new Task();
        task2.setTitle("task 2");
        task2.setDescription("description task 2");
        task2.setUser(user);
        task2.setCompleted(true);

        completedTasks = List.of(task2);
        incompletedTasks = List.of(task);
    }

    @Test
    public void whenCreateCompletedTaskReportThenOk() {
        String exceptedBody = "You have 1 completed tasks: task 2";
        KafkaMessageDto kafkaMessageDto = kafkaMessageCreator.create(true, completedTasks, user);
        assertThat(kafkaMessageDto.email()).isEqualTo(user.getEmail());
        assertThat(kafkaMessageDto.body()).isEqualTo(exceptedBody);
    }

    @Test
    public void whenCreateIncompletedTaskReportThenOk() {
        String exceptedBody = "You have 1 incompleted tasks: task 1";
        KafkaMessageDto kafkaMessageDto = kafkaMessageCreator.create(false, incompletedTasks, user);
        assertThat(kafkaMessageDto.email()).isEqualTo(user.getEmail());
        assertThat(kafkaMessageDto.body()).isEqualTo(exceptedBody);
    }
}