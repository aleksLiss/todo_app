package com.todo.app.email_sender.service;

import com.google.gson.Gson;
import com.todo.app.email_sender.config.SmtpProperties;
import com.todo.app.email_sender.dto.KafkaMessageDto;
import com.todo.app.email_sender.mapper.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final JsonMapper jsonMapper;
    private final EmailMessagingService emailMessagingService;
    private final Gson gson;
    private final SmtpProperties smtpProperties;

    @KafkaListener(topics = "${KAFKA_TOPIC_NAME}", groupId = "${KAFKA_GROUP_ID}")
    public void receiveMessage(String message){
        KafkaMessageDto messageDto = jsonMapper.fromJson(gson, message);
        emailMessagingService.sendEmail(
                smtpProperties.from(),
                messageDto.email(),
                messageDto.message()
        );
    }
}
