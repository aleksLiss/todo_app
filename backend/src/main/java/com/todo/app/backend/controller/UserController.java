package com.todo.app.backend.controller;

import com.todo.app.backend.dto.user.GetUserResponseDto;
import com.todo.app.backend.dto.JwtTokenResponseDto;
import com.todo.app.backend.dto.user.SignUpUserDto;
import com.todo.app.backend.security.UserPrincipal;
import com.todo.app.backend.service.JwtService;
import com.todo.app.backend.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<@NonNull JwtTokenResponseDto> signUp(@RequestBody SignUpUserDto signUpUserDto) {
        UserPrincipal userPrincipal = userService.save(signUpUserDto);
        String token = jwtService.generateToken(userPrincipal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }

    @GetMapping
    public ResponseEntity<@NonNull GetUserResponseDto> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        GetUserResponseDto responseDto = new GetUserResponseDto(
                userPrincipal.id(),
                userPrincipal.email()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }
}
