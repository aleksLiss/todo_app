package com.todo.app.backend.service;

import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.dto.user.SignUpUserDto;
import com.todo.app.backend.exception.user.UserAlreadyExists;
import com.todo.app.backend.exception.user.UserNotFoundException;
import com.todo.app.backend.model.User;
import com.todo.app.backend.repository.UserRepository;
import com.todo.app.backend.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageCreator messageCreator;
    private final SenderService senderService;

    public UserPrincipal save(SignUpUserDto signUpUserDto) {
        ifUsernameExists(signUpUserDto.email());
        User user = new User();
        user.setEmail(signUpUserDto.email());
        user.setPassword(passwordEncoder.encode(signUpUserDto.password()));
        User savedUser = userRepository.save(user);
        KafkaMessageDto kafkaMessageDto = messageCreator.create(savedUser);
        senderService.send(kafkaMessageDto);
        return new UserPrincipal(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getPassword()
        );
    }

    public UserPrincipal getUserByEmail(String email) {
        User foundUser = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserPrincipal(
                foundUser.getId(),
                foundUser.getEmail(),
                foundUser.getPassword()
        );
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) {
        User foundUser = userRepository.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                foundUser.getEmail(),
                foundUser.getPassword(),
                List.of());
    }

    private void ifUsernameExists(String username) {
        userRepository.getUserByEmail(username)
                .ifPresent(user -> {
                    throw new UserAlreadyExists("user already exists");
                });
    }
}