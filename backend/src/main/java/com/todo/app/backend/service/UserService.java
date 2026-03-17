package com.todo.app.backend.service;

import com.todo.app.backend.dto.RequestUserDto;
import com.todo.app.backend.exception.UserAlreadyExists;
import com.todo.app.backend.exception.UserNotFoundException;
import com.todo.app.backend.model.User;
import com.todo.app.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(RequestUserDto requestUserDto) {
        User user = new User();
        user.setEmail(requestUserDto.email());
        user.setPassword(requestUserDto.password());
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new UserAlreadyExists("user already exists");
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
