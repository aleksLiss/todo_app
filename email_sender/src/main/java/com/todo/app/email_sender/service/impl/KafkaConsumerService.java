package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import com.todo.app.email_sender.model.MessageReceivedEvent;
import com.todo.app.email_sender.service.ConsumerService;
import com.todo.app.email_sender.service.CreatorService;
import com.todo.app.email_sender.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService implements ConsumerService {

    private final ApplicationEventPublisher publisher;

    @KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "email-sender-group")
    @Override
    public void receive(KafkaMessageDto kafkaMessageDto) {
        publisher.publishEvent(new MessageReceivedEvent(kafkaMessageDto));
    }
}
