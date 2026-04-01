package com.todo.app.email_sender.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "${KAFKA_TOPIC_NAME}", groupId = "${KAFKA_GROUP_ID}")
    public void receiveMessage(String message){
        log.info("Сообщение из Kafka получено автоматически: {}", message);
    }
}
