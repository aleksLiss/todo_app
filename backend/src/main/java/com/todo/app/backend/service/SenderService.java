package com.todo.app.backend.service;

import com.todo.app.backend.dto.KafkaMessageDto;

public interface SenderService {

    void send(KafkaMessageDto kafkaMessageDto);
}
