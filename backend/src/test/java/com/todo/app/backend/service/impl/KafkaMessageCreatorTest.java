package com.todo.app.backend.service.impl;

import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageCreatorTest {

    @InjectMocks
    private KafkaMessageCreator kafkaMessageCreator;
    private User user;

    @BeforeEach
    public void setUp() {
        String email = "aleks@google.com";
        user = new User();
        user.setEmail(email);
        user.setPassword("password");
    }

    @Test
    public void whenCreateKafkaMessageThenOk() {
        KafkaMessageDto createMessage = kafkaMessageCreator.create(user);
        assertThat(createMessage).isNotNull();
        assertThat(createMessage.email()).isEqualTo(user.getEmail());
    }
}