package com.todo.app.backend.controller;

import com.todo.app.backend.dto.task.SaveTaskDto;
import com.todo.app.backend.dto.task.UpdateTaskDto;
import com.todo.app.backend.security.UserPrincipal;
import com.todo.app.backend.service.TaskService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<@NonNull List<@NonNull UpdateTaskDto>> getTasksByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<UpdateTaskDto> tasks = taskService.getListTasksByUser(userPrincipal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tasks);
    }

    @PostMapping
    public ResponseEntity<@NonNull Void> createTask(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @RequestBody SaveTaskDto saveTaskDto) {
        taskService.save(saveTaskDto, userPrincipal);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<@NonNull Void> deleteTask(@PathVariable UUID taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<@NonNull UpdateTaskDto> completeTask(@PathVariable UUID taskId,
                                                               @RequestBody UpdateTaskDto updateTaskDto) {
        UpdateTaskDto updatedTask = taskService.updateTaskById(taskId, updateTaskDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedTask);
    }
}
