package com.todo.app.backend.service;

import com.todo.app.backend.config.KafkaProperties;
import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.exception.KafkaSendMessageException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessagingService {

    private final KafkaTemplate<@NonNull String,@NonNull String> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void sendMessage(KafkaMessageDto kafkaMessageDto) {
        kafkaTemplate.send(kafkaProperties.topicName(), kafkaMessageDto.message())
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        throw new KafkaSendMessageException(ex.getMessage());
                    }
                });
    }
}
