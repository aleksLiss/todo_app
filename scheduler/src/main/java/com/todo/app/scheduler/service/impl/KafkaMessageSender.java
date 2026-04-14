package com.todo.app.scheduler.service.impl;

import com.todo.app.scheduler.config.KafkaProperties;
import com.todo.app.scheduler.dto.KafkaMessageDto;
import com.todo.app.scheduler.exception.KafkaSendMessageException;
import com.todo.app.scheduler.service.MessageSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageSender implements MessageSender {

    private final KafkaTemplate<@NonNull String, @NonNull KafkaMessageDto> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public void sendMessage(KafkaMessageDto kafkaMessageDto) {
        kafkaTemplate.send(kafkaProperties.topicName(), kafkaMessageDto)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        throw new KafkaSendMessageException("Exception during sending kafka message");
                    }
                });
    }
}
