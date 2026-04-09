package com.todo.app.email_sender.service;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import org.springframework.mail.SimpleMailMessage;

public interface CreatorService {

    SimpleMailMessage create(KafkaMessageDto kafkaMessageDto);
}
