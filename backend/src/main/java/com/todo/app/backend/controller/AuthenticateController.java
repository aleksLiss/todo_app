package com.todo.app.backend.controller;

import com.todo.app.backend.dto.JwtTokenResponseDto;
import com.todo.app.backend.dto.RequestUserDto;
import com.todo.app.backend.security.UserPrincipal;
import com.todo.app.backend.service.JwtService;
import com.todo.app.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateController {

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<@NonNull JwtTokenResponseDto> loginUser(@Valid @ModelAttribute RequestUserDto requestUserDto) {
        UserPrincipal userPrincipal = userService.getUserByEmail(requestUserDto.email());
        String token = jwtService.generateToken(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponseDto(token));
    }
}
