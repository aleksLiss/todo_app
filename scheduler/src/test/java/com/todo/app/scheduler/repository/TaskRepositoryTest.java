package com.todo.app.scheduler.repository;

import com.todo.app.scheduler.model.Task;
import com.todo.app.scheduler.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    private Task oldTask;
    private Task newTask;
    private User user;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
        user = new User();
        user.setEmail("aleks@gmail.com");
        user.setPassword("12345");
        userRepository.save(user);

        oldTask = new Task();
        oldTask.setTitle("old_task");
        oldTask.setDescription("description");
        oldTask.setUser(user);
        oldTask.setCreatedAt(LocalDateTime.now().minusDays(100));

        newTask = new Task();
        newTask.setTitle("new_task");
        newTask.setDescription("description");
        newTask.setUser(user);
        newTask.setCreatedAt(LocalDateTime.now().minusDays(1));

        taskRepository.save(oldTask);
        taskRepository.save(newTask);
    }

    @Test
    public void whenSaveTaskThenOk() {
        Optional<Task> savedTask = Optional.of(taskRepository.save(oldTask));
        assertThat(savedTask).isPresent();
        assertThat(savedTask.get().getUser()).isEqualTo(user);
    }

    @Test
    public void whenGetTaskThenReturnSavedTask() {
        Optional<Task> savedTask = Optional.of(taskRepository.save(oldTask));
        assertThat(savedTask).isPresent();
        List<Task> savedTasks = taskRepository.getTaskByTitle("old_task");
        assertThat(savedTasks)
                .isNotEmpty()
                .hasSize(1)
                .contains(oldTask);
    }

    @Test
    public void whenGetTaskBetweenCreatedAtThenReturnOneTask() {
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();
        List<Task> savedTasks = taskRepository.findAllByCreatedAtBetweenWithUser(start, end);
        assertThat(savedTasks)
                .isNotEmpty()
                .hasSize(1)
                .contains(newTask);
    }
}