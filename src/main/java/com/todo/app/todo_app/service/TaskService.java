package com.todo.app.todo_app.service;

import com.todo.app.todo_app.dto.CreateTaskDto;
import com.todo.app.todo_app.dto.TaskResponseDto;
import com.todo.app.todo_app.dto.UpdateTaskDto;
import com.todo.app.todo_app.exception.TaskAlreadyExistsException;
import com.todo.app.todo_app.exception.TaskNotFoundException;
import com.todo.app.todo_app.exception.UserNotFoundException;
import com.todo.app.todo_app.mapper.TaskMapper;
import com.todo.app.todo_app.model.Task;
import com.todo.app.todo_app.model.User;
import com.todo.app.todo_app.repository.TaskRepository;
import com.todo.app.todo_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public void completeTask() {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        taskRepository.completedTask(foundUser);
    }

    public void updateTask(UpdateTaskDto updateTaskDto) {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        taskRepository.getTasksByTitleAndCreatedBy(
                        updateTaskDto.oldTitle(),
                        foundUser
                )
                .orElseThrow(() ->
                        new TaskNotFoundException("Task with title '" + updateTaskDto.oldTitle() + "' not found"));
        taskRepository.updateTitleAndDescription(
                foundUser,
                updateTaskDto.oldTitle(),
                updateTaskDto.newTitle(),
                updateTaskDto.newDescription());
    }

    public TaskResponseDto getTaskByTitle(String title) {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        Optional<Task> task = Optional.ofNullable(taskRepository.getTasksByTitleAndCreatedBy(title, foundUser)
                .orElseThrow(() -> new TaskNotFoundException("Task with title '" + title + "' not found")));
        return new TaskResponseDto(
                task.get().getTitle(),
                task.get().getDescription(),
                String.valueOf(task.get().isCompleted()),
                String.valueOf(task.get().getCompletedAt())
        );
    }

    public List<TaskResponseDto> getAllCompletedTasks() {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        List<Task> tasks = taskRepository.findAllByCreatedBy(foundUser);
        List<TaskResponseDto> result = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                continue;
            }
            result.add(new TaskResponseDto(
                    task.getTitle(),
                    task.getDescription(),
                    String.valueOf(true),
                    String.valueOf(task.getCompletedAt())
            ));
        }
        return result;
    }

    public List<TaskResponseDto> getAllTasks() {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        List<Task> tasks = taskRepository.findAllByCreatedBy(foundUser);
        List<TaskResponseDto> result = new ArrayList<>();
        for (Task task : tasks) {
            result.add(
                    new TaskResponseDto(
                            task.getTitle(),
                            task.getDescription(),
                            String.valueOf(task.isCompleted()),
                            String.valueOf(task.getCompletedAt())
                    )
            );
        }
        return result;
    }

    public void deleteTaskByTitle(String title) {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        taskRepository.deleteTaskByTitleAndCreatedBy(title, foundUser);
    }

    public void deleteAllTasks() {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        taskRepository.deleteAllByCreatedBy(foundUser);
    }

    @Transactional
    public void createTask(CreateTaskDto createTaskDto) {
        String email = getEmailFromContext();
        User foundUser = getUserByEmail(email);
        Task task = taskMapper.toTask(createTaskDto);
        task.setCreatedBy(foundUser);
        try {
            taskRepository.save(task);
        } catch (Exception e) {
            throw new TaskAlreadyExistsException("Task already exists");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private String getEmailFromContext() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
    }
}