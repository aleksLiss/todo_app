package com.todo.app.todo_app.controller;

import com.todo.app.todo_app.dto.request.JwtRequest;
import com.todo.app.todo_app.service.UserService;
import com.todo.app.todo_app.util.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticateController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth/login")
    public ResponseEntity<@NonNull Map<String, String>> signIn(@Valid @RequestBody JwtRequest jwtRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.email(),
                        jwtRequest.password()
                )
        );
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.email());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of("token", token));
    }
}
