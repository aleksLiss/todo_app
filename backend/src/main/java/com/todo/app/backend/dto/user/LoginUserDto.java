package com.todo.app.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginUserDto(
        @NotBlank(message = "Email must be not empty")
        @Length(message = "Email length must be from 5 to 50", min = 5, max = 50)
        @Email(message = "Email must be like 'max@google.com'")
        String email,
        @NotBlank(message = "Password must be not empty")
        @Length(message = "Password length must be from 8 to 100", min = 8, max = 100)
        String password
) {
}
