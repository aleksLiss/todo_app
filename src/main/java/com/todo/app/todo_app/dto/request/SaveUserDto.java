package com.todo.app.todo_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SaveUserDto {

    @NotBlank(message = "Email must be not empty")
    @Email(message = "Email must be equals like 'max@google.com'")
    private String email;
    @NotBlank(message = "Password must be not empty")
    @Length(min = 8, max = 20, message = "Password length must be from 8 to 20 symbols")
    private String password;
}
