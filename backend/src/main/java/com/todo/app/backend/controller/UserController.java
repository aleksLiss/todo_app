package com.todo.app.backend.controller;

import com.todo.app.backend.dto.GetUserResponseDto;
import com.todo.app.backend.dto.JwtTokenResponseDto;
import com.todo.app.backend.dto.RequestUserDto;
import com.todo.app.backend.security.UserPrincipal;
import com.todo.app.backend.service.JwtService;
import com.todo.app.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/user")
    public ResponseEntity<@NonNull JwtTokenResponseDto> signUp(@Valid @ModelAttribute RequestUserDto requestUserDto) {
        UserPrincipal userPrincipal = userService.save(requestUserDto);
        String token = jwtService.generateToken(userPrincipal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtTokenResponseDto(token));
    }

    @GetMapping("/user")
    public ResponseEntity<@NonNull GetUserResponseDto> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        GetUserResponseDto responseDto = new GetUserResponseDto(
                userPrincipal.id(),
                userPrincipal.email()
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDto);
    }
}
