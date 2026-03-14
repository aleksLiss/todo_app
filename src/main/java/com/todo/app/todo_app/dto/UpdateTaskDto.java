package com.todo.app.todo_app.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateTaskDto(
        @NotBlank(message = "Old title must be not empty")
        String oldTitle,
        @NotBlank(message = "New title must be not empty")
        @Length(min = 1, max = 100, message = "Length new title must be from 1 to 100")
        String newTitle,
        String newDescription
) {
}
