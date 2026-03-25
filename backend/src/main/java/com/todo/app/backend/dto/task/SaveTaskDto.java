package com.todo.app.backend.dto.task;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SaveTaskDto(
        @NotBlank(message = "Title must be not empty")
        @Length(message = "Length title from 1 to 50 characters", min = 1, max = 50)
        String title,
        @Length(message = "Max length description 255 characters", max = 255)
        String description
) {
}
