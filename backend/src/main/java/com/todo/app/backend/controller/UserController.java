package com.todo.app.backend.controller;

import com.todo.app.backend.dto.GetUserResponseDto;
import com.todo.app.backend.dto.SignUpResponseDto;
import com.todo.app.backend.dto.RequestUserDto;
import com.todo.app.backend.mapper.ResponseDtoMapper;
import com.todo.app.backend.model.User;
import com.todo.app.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ResponseDtoMapper responseDtoMapper;

    @PostMapping("/user")
    public ResponseEntity<@NonNull SignUpResponseDto> signUp(@Valid @ModelAttribute RequestUserDto requestUserDto) {
        userService.save(requestUserDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SignUpResponseDto("token example"));
    }

    @GetMapping("/user")
    public ResponseEntity<@NonNull GetUserResponseDto> getUserByEmail(@Valid @ModelAttribute RequestUserDto requestUserDto) {
        User foundUser = userService.getUserByEmail(requestUserDto.email());
        GetUserResponseDto getUserResponseDto = responseDtoMapper.toGetUserResponseDto(foundUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(getUserResponseDto);
    }
}
