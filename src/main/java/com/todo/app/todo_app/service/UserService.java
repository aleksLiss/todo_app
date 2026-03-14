package com.todo.app.todo_app.service;

import com.todo.app.todo_app.dto.request.SaveUserDto;
import com.todo.app.todo_app.exception.SaveUserException;
import com.todo.app.todo_app.exception.UserAlreadyExistsException;
import com.todo.app.todo_app.exception.UserNotFoundException;
import com.todo.app.todo_app.mapper.UserMapper;
import com.todo.app.todo_app.model.User;
import com.todo.app.todo_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("No user found with username '%s'.", email)
                ));
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) {
        return userRepository.findUserByEmail(username)
                .map(userMapper::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("No user found with username '%s'.", username)
                ));
    }

    @Transactional
    public Optional<User> saveUser(SaveUserDto saveUserDto) {
        if (userRepository.existsUserByEmail(saveUserDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        User user = userMapper.toUser(saveUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Optional.ofNullable(Optional.of(userRepository.save(user))
                .orElseThrow(() -> new SaveUserException("Internal server error")));
    }
}
