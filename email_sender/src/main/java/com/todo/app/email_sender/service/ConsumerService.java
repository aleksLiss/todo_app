package com.todo.app.email_sender.service;

import com.todo.app.email_sender.dto.KafkaMessageDto;

public interface ConsumerService {

    void receive(KafkaMessageDto kafkaMessageDto);
}
