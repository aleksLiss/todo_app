package com.todo.app.backend.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateTaskDto(
        UUID id,
        String title,
        String description,
        @JsonProperty("completed")
        boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime completedAt
) {
    public UpdateTaskDto() {
        this(null, null, null, false, null, null);
    }
}