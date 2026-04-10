package com.todo.app.backend.service.impl;

import com.todo.app.backend.config.KafkaProperties;
import com.todo.app.backend.dto.KafkaMessageDto;
import com.todo.app.backend.exception.KafkaSendMessageException;
import com.todo.app.backend.service.SenderService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessagingService implements SenderService {

    private final KafkaTemplate<@NonNull String,@NonNull KafkaMessageDto> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    @Override
    public void send(KafkaMessageDto kafkaMessageDto) {
        kafkaTemplate.send(kafkaProperties.topicName(), kafkaMessageDto)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        throw new KafkaSendMessageException(ex.getMessage());
                    }
                });
    }
}
