package com.todo.app.todo_app.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateTaskDto(
        @NotBlank(message = "Title must be not empty")
        @Length(min = 1, max = 100, message = "Title mu be length from 1 to 100")
        String title,
        @Length(max = 255, message = "Description max length = 255")
        String description
) {
}
