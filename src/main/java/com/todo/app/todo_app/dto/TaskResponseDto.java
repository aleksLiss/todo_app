package com.todo.app.todo_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TaskResponseDto(
        String title,
        String description,
        String isCompleted,
        String completedAt
) {
}
