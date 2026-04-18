package com.todo.app.backend.service;

import com.todo.app.backend.dto.task.SaveTaskDto;
import com.todo.app.backend.dto.task.UpdateTaskDto;
import com.todo.app.backend.exception.task.DeleteTaskException;
import com.todo.app.backend.exception.task.TaskNotFoundException;
import com.todo.app.backend.mapper.TaskDtoMapper;
import com.todo.app.backend.model.Task;
import com.todo.app.backend.model.User;
import com.todo.app.backend.repository.TaskRepository;
import com.todo.app.backend.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskDtoMapper taskDtoMapper;
    @InjectMocks
    private TaskService taskService;
    private User user;
    private Task task;
    private UserPrincipal userPrincipal;
    private UpdateTaskDto updateTaskDto;

    @BeforeEach
    public void setUp() {
        String email = "email";
        String password = "password";
        UUID uuid = UUID.randomUUID();
        user = new User();
        user.setId(uuid);
        user.setEmail(email);
        user.setPassword(password);
        userPrincipal = new UserPrincipal(uuid, email, password);
        String title = "title";
        String description = "description";
        UUID taskId = UUID.randomUUID();
        task = new Task();
        task.setId(taskId);
        task.setTitle(title);
        task.setDescription(description);
        task.setCreatedBy(user);
        String newTitle = "new_title";
        String newDescription = "new_description";
        UUID newTaskId = UUID.randomUUID();
        updateTaskDto = new UpdateTaskDto(
                newTaskId,
                newTitle,
                newDescription,
                true,
                null,
                null
        );
    }

    @Test
    public void whenSaveTaskThenOk() {
        String title = "title";
        String description = "description";
        SaveTaskDto saveTaskDto = new SaveTaskDto(title, description);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        taskService.save(saveTaskDto, userPrincipal);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void whenUpdateTaskThenOk() {
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskDtoMapper.taskDtoToUpdateTaskDto(any(Task.class))).thenReturn(updateTaskDto);
        UpdateTaskDto updated = taskService.updateTaskById(updateTaskDto.id(), updateTaskDto);
        assertThat(updated).isNotNull();
        assertThat(updated.title()).isEqualTo(updateTaskDto.title());
        assertThat(updated.description()).isEqualTo(updateTaskDto.description());
        verify(taskRepository).findById(updateTaskDto.id());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void whenUpdateTaskThenThrowException() {
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        assertThatThrownBy(() -> taskService.updateTaskById(UUID.randomUUID(), updateTaskDto))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    public void whenDeleteTaskByUUIDThenOk() {
        doNothing().when(taskRepository).deleteTaskId(task.getId());
        taskService.deleteTaskById(task.getId());
        verify(taskRepository).deleteTaskId(task.getId());
    }

    @Test
    public void whenDeleteThenThrowException() {
        doThrow(new RuntimeException()).when(taskRepository).deleteTaskId(any(UUID.class));
        assertThatThrownBy(() -> taskService.deleteTaskById(task.getId()))
                .isInstanceOf(DeleteTaskException.class);
        verify(taskRepository).deleteTaskId(task.getId());
    }

    @Test
    public void whenGetListUpdateDtoTasksThenOk() {
        when(taskRepository.findAllByCreatedBy(user)).thenReturn(List.of(updateTaskDto));
        List<UpdateTaskDto> list = taskService.getListTasksByUser(userPrincipal);
        assertThat(list)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .containsExactlyInAnyOrder(updateTaskDto);
    }

    @Test
    public void whenGetListUpdateDtoTasksThenReturnEmptyList() {
        when(taskRepository.findAllByCreatedBy(any(User.class))).thenReturn(List.of());
        List<UpdateTaskDto> list = taskService.getListTasksByUser(userPrincipal);
        assertThat(list)
                .isEmpty();
    }
}