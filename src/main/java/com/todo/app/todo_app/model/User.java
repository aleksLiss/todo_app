package com.todo.app.todo_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "Email must be not empty")
    @Email
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @NotBlank(message = "Password must be not empty")
    @Length(min = 8, max = 100, message = "Length must be from 8 to 20 symbols")
    @Column(name = "password", nullable = false)
    private String password;
}
