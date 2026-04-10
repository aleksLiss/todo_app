package com.todo.app.backend.service;

import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.model.User;

public interface MessageCreator {

    KafkaMessageDto create(User user);
}
