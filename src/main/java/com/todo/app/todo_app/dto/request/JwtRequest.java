package com.todo.app.todo_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record JwtRequest(
        @NotBlank(message = "Email must be not empty")
        @Email(message = "Email must be like 'qwe@gmail.com'")
        String email,
        @NotBlank(message = "Password must be not empty")
        @Length(min = 8, max = 100, message = "Password length must be from 8 to 100")
        String password) {
}
