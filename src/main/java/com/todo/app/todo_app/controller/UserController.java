package com.todo.app.todo_app.controller;

import com.todo.app.todo_app.dto.request.SaveUserDto;
import com.todo.app.todo_app.dto.request.GetUserDto;
import com.todo.app.todo_app.model.User;
import com.todo.app.todo_app.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Data
public class UserController {

    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<@NonNull Map<String, String>> signUpUser(@Valid @ModelAttribute("user") SaveUserDto saveUserDto) {
        userService.saveUser(saveUserDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("CUSTOM-TOKEN", "EXAMPLE-TOKEN"));
    }

    @GetMapping("/user")
    public ResponseEntity<@NonNull Map<String, String>> getUser(@Valid @ModelAttribute("user") GetUserDto userDto) {
        User foundUser = userService.getUser(userDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("id: ", foundUser.getId().toString(),
                        "email: ", foundUser.getEmail()));
    }
}
