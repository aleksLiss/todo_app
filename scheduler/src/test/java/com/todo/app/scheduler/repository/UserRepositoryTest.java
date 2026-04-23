package com.todo.app.scheduler.repository;

import com.todo.app.scheduler.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    private User user;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail("aleks@gmail.com");
        user.setPassword("1234");
    }

    @Test
    public void saveUser() {
        Optional<User> savedUser = Optional.of(userRepository.save(user));
        assertThat(savedUser).isPresent();
    }

    @Test
    public void whenFindUserByEmailThenOk() {
        String email = "aleks@gmail.com";
        userRepository.save(user);
        Optional<User> foundUserByEmail = userRepository.getUserByEmail(email);
        assertThat(foundUserByEmail).isPresent();
    }
}