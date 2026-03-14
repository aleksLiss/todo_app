package com.todo.app.todo_app.controller;

import com.todo.app.todo_app.dto.CreateTaskDto;
import com.todo.app.todo_app.dto.TaskDto;
import com.todo.app.todo_app.dto.TaskResponseDto;
import com.todo.app.todo_app.dto.UpdateTaskDto;
import com.todo.app.todo_app.service.TaskService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/save")
    public ResponseEntity<@NonNull Map<String, String>> createTask(@Valid @ModelAttribute CreateTaskDto createTaskDto) {
        taskService.createTask(createTaskDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message: ", "Task create successfully"
                ));
    }

    @GetMapping("/one")
    public ResponseEntity<@NonNull TaskResponseDto> getTaskByTitle(@Valid @ModelAttribute TaskDto taskDto) {
        TaskResponseDto responseDto = taskService.getTaskByTitle(taskDto.getTitle());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<@NonNull List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks());
    }

    @GetMapping("/all/completed")
    public ResponseEntity<@NonNull List<TaskResponseDto>> getAllCompletedTasks() {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllCompletedTasks());
    }

    @PutMapping("/one")
    public ResponseEntity<?> updateTask(@Valid @ModelAttribute UpdateTaskDto updateTaskDto) {
        taskService.updateTask(updateTaskDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/one")
    public ResponseEntity<?> completeTask() {
        taskService.completeTask();
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllTasks() {
        taskService.deleteAllTasks();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/one")
    public ResponseEntity<@NonNull Map<String, String>> deleteTask(@Valid @ModelAttribute TaskDto taskDto) {
        taskService.deleteTaskByTitle(taskDto.getTitle());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
