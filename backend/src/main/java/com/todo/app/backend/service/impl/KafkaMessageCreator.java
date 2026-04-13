package com.todo.app.backend.service.impl;

import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.model.User;
import com.todo.app.backend.service.MessageCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageCreator implements MessageCreator {

    @Override
    public KafkaMessageDto create(User user) {
        String email = user.getEmail();
        String title = "Sign up successfully!";
        String body = String.format("Welcome %s to TODO application!", email);
        log.warn("CREATE MESSAGE INTO BACKEND========================================!!!!!!!!!!");
        return new KafkaMessageDto(email, title, body);
    }
}
