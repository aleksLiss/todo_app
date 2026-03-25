package com.todo.app.backend.controller;

import com.todo.app.backend.dto.user.LoginUserDto;
import com.todo.app.backend.security.UserPrincipal;
import com.todo.app.backend.service.JwtService;
import com.todo.app.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticateController {

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserDto loginUserDto) {
        UserPrincipal userPrincipal = userService.getUserByEmail(loginUserDto.email());
        String token = jwtService.generateToken(userPrincipal);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();
    }
}
