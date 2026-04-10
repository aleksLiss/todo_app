package com.todo.app.backend.service.impl;

import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.model.User;
import com.todo.app.backend.service.MessageCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageCreator implements MessageCreator {

    @Override
    public KafkaMessageDto create(User user) {
        String email = user.getEmail();
        String title = "Sign up successfully!";
        String body = String.format("Welcome %s to TODO application!", email);
        return new KafkaMessageDto(email, title, body);
    }
}
