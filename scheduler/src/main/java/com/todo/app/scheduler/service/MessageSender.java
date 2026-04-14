package com.todo.app.scheduler.service;

import com.todo.app.scheduler.dto.KafkaMessageDto;

public interface MessageSender {

    void sendMessage(KafkaMessageDto kafkaMessageDto);
}
