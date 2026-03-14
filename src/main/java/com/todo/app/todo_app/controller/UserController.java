package com.todo.app.todo_app.controller;

import com.todo.app.todo_app.dto.request.SaveUserDto;
import com.todo.app.todo_app.model.User;
import com.todo.app.todo_app.service.UserService;
import com.todo.app.todo_app.util.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api")
@Data
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/user")
    public ResponseEntity<@NonNull Map<String, String>> signUpUser(@Valid @ModelAttribute("user") SaveUserDto saveUserDto) {
        User user = userService.saveUser(saveUserDto).get();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        saveUserDto.getEmail(),
                        saveUserDto.getPassword()
                )
        );
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("JWT_ACCESS_TOKEN", token));
    }

    @GetMapping("/user")
    public ResponseEntity<@NonNull Map<String, String>> getUserIdAndEmail() {
        String email =
                Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User foundUser = userService.findUserByEmail(email);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id: ", foundUser.getId().toString());
        map.put("email: ", foundUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(map);
    }
}
