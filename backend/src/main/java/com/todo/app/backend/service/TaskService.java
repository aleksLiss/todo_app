package com.todo.app.backend.service;

import aQute.bnd.annotation.jpms.Open;
import com.todo.app.backend.dto.task.SaveTaskDto;
import com.todo.app.backend.dto.task.UpdateTaskDto;
import com.todo.app.backend.exception.task.DeleteTaskException;
import com.todo.app.backend.exception.task.TaskAlreadyExists;
import com.todo.app.backend.exception.task.TaskNotFoundException;
import com.todo.app.backend.exception.task.UpdateTaskException;
import com.todo.app.backend.exception.user.UserNotFoundException;
import com.todo.app.backend.mapper.TaskDtoMapper;
import com.todo.app.backend.model.Task;
import com.todo.app.backend.model.User;
import com.todo.app.backend.repository.TaskRepository;
import com.todo.app.backend.repository.UserRepository;
import com.todo.app.backend.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskDtoMapper taskDtoMapper;

    public void save(SaveTaskDto saveTaskDto, UserPrincipal userPrincipal) {
        User foundUser = getUserByEmail(userPrincipal);
        Task task = new Task();
        task.setTitle(saveTaskDto.title());
        task.setDescription(saveTaskDto.description());
        task.setCreatedBy(foundUser);
        taskRepository.save(task);
    }

    public UpdateTaskDto updateTaskById(UUID taskId, UpdateTaskDto updateTaskDto) {
        log.warn("updateTaskById " +  updateTaskDto.toString());
        Task task = taskRepository.findById(taskId)
                .orElseThrow(
                        TaskNotFoundException::new
                );
        task.setTitle(updateTaskDto.title());
        task.setDescription(updateTaskDto.description());
        task.setCompleted(updateTaskDto.isCompleted());
        if (updateTaskDto.isCompleted()) {
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompletedAt(null);
        }
        Task updatedTask = taskRepository.save(task);
        return taskDtoMapper.taskDtoToUpdateTaskDto(updatedTask);
    }

    public void deleteTaskById(UUID taskId) {
        try {
            taskRepository.deleteTaskId(taskId);
        } catch (Exception ex) {
            throw new DeleteTaskException();
        }
    }

    public List<UpdateTaskDto> getListTasksByUser(UserPrincipal userPrincipal) {
        User foundUser = getUserByEmail(userPrincipal);
        return taskRepository.findAllByCreatedBy(foundUser);
    }

    private User getUserByEmail(UserPrincipal userPrincipal) {
        return userRepository.getUserByEmail(userPrincipal.email())
                .orElseThrow(
                        UserNotFoundException::new
                );
    }
}
