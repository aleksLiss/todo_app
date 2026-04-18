package com.todo.app.backend.repository;

import com.todo.app.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(BCryptPasswordEncoder.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    public void setUp() {
        String email = "aleks@google.com";
        String password = "password";
        user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
    }

    @Test
    public void whenSaveUserThenOk() {
        User savedUser = userRepository.save(user);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenFindUserByEmailThenOk() {
        userRepository.save(user);
        Optional<User> foundUser = userRepository.getUserByEmail(user.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
    }
}