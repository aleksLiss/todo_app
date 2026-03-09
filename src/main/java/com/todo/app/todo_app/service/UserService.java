package com.todo.app.todo_app.service;

import com.todo.app.todo_app.dto.request.SaveUserDto;
import com.todo.app.todo_app.dto.request.GetUserDto;
import com.todo.app.todo_app.exception.UserAlreadyExistsException;
import com.todo.app.todo_app.exception.UserNotFoundException;
import com.todo.app.todo_app.mapper.UserMapper;
import com.todo.app.todo_app.model.User;
import com.todo.app.todo_app.repository.UserRepository;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    public User getUser(GetUserDto getUserDto) {
        User user = userRepository.findUserByEmail(getUserDto.getEmail());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }

    public void saveUser(SaveUserDto userDto) {
        if (isUserExist(userDto)) {
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    private boolean isUserExist(SaveUserDto userDto) {
        return userRepository.existsUserByEmail(userDto.getEmail());
    }
}
