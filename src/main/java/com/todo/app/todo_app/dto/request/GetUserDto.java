package com.todo.app.todo_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetUserDto {
    @NotBlank(message = "Email must be not empty")
    @Email
    private String email;
}
