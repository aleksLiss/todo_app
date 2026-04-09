package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import com.todo.app.email_sender.service.ConsumerService;
import com.todo.app.email_sender.service.CreatorService;
import com.todo.app.email_sender.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService implements ConsumerService {

    private final CreatorService creatorService;
    private final SenderService senderService;

    @KafkaListener(topics = "${KAFKA_TOPIC_NAME}", groupId = "${KAFKA_GROUP_ID}")
    @Override
    public void receive(KafkaMessageDto kafkaMessageDto) {
        SimpleMailMessage message = creatorService.create(kafkaMessageDto);
        senderService.sendEmail(message);
    }
}
