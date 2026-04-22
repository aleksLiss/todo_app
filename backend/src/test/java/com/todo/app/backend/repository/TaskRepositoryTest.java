package com.todo.app.backend.repository;

import com.todo.app.backend.dto.task.UpdateTaskDto;
import com.todo.app.backend.model.Task;
import com.todo.app.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User user;
    private Task taskOne;
    private Task taskTwo;

    @BeforeEach
    public void setup() {
        String email = "aleks@google.com";
        String password = "password";
        user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        String title = "title";
        String description = "description";
        taskOne = new Task();
        taskOne.setTitle(title);
        taskOne.setDescription(description);
        taskOne.setCreatedBy(user);
        taskTwo = new Task();
        taskTwo.setTitle(title);
        taskTwo.setDescription(description);
        taskTwo.setCreatedBy(user);
    }

    @Test
    public void whenSaveTaskThenOk() {
        Optional<Task> savedTask = Optional.of(taskRepository.save(taskOne));
        assertThat(savedTask).isPresent();
    }

    @Test
    public void whenFindAllByCreatedByUserThenOk() {
        taskRepository.save(taskOne);
        taskRepository.save(taskTwo);
        List<UpdateTaskDto> findAllByCreatedBy = taskRepository.findAllByCreatedBy(user);
        assertThat(findAllByCreatedBy)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void whenDeleteTaskByUUIDThenOk() {
        Optional<Task> savedTask = Optional.of(taskRepository.save(taskOne));
        assertThat(savedTask).isPresent();
        UUID uuid = savedTask.get().getId();
        taskRepository.deleteById(uuid);
        assertThat(taskRepository.findById(uuid)).isEmpty();
    }
}