package com.todo.app.todo_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class TaskDto {
    @NotBlank(message = "Title must be not empty")
    @Length(min = 1, max = 100, message = "Title mu be length from 1 to 100")
    private String title;
}
