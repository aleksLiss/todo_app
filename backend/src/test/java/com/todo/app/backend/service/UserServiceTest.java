package com.todo.app.backend.service;

import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.dto.user.SignUpUserDto;
import com.todo.app.backend.exception.user.UserNotFoundException;
import com.todo.app.backend.model.User;
import com.todo.app.backend.repository.UserRepository;
import com.todo.app.backend.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MessageCreator messageCreator;
    @Mock
    private SenderService senderService;
    @InjectMocks
    private UserService userService;
    private User user;
    private SignUpUserDto signUpUserDto;
    private KafkaMessageDto kafkaMessageDto;

    @BeforeEach
    public void setup() {
        String email = "aleks@google.com";
        String password = "password";
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setPassword("encoded_password");
        String title = "title";
        String body = "body";
        kafkaMessageDto = new KafkaMessageDto(email, title, body);
        signUpUserDto = new SignUpUserDto(email, password, password);
    }

    @Test
    public void whenSaveUserThenOk() {
        when(userRepository.getUserByEmail(signUpUserDto.email())).thenReturn(Optional.empty());
        when(messageCreator.create(any(User.class))).thenReturn(kafkaMessageDto);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserPrincipal userPrincipal = userService.save(signUpUserDto);
        assertThat(userPrincipal).isNotNull();
        assertThat(userPrincipal.getUsername()).isEqualTo(user.getEmail());
        verify(userRepository).save(any(User.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void whenFindUserByEmailThenReturnUserPrincipal() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserPrincipal principal = userService.getUserByEmail(user.getEmail());
        assertThat(principal).isNotNull();
    }

    @Test
    public void whenNotFoundUserByEmailThenThrowUserNotFoundException() {
        when(userRepository.getUserByEmail(any(String.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getUserByEmail("email"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void whenLoadByUsernameThenReturnUserDetails() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        assertThat(userDetails).isNotNull();
    }

    @Test
    public void whenNotLoadByUsernameThenThrowUserNotFoundException() {
        when(userRepository.getUserByEmail(any(String.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.loadUserByUsername("email"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
}