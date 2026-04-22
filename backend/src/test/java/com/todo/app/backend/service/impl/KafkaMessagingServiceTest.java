package com.todo.app.backend.service.impl;

import com.todo.app.backend.config.KafkaProperties;
import com.todo.app.backend.dto.KafkaMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessagingServiceTest {

    @Mock
    private KafkaTemplate<String, KafkaMessageDto> kafkaTemplate;
    @Mock
    private KafkaProperties kafkaProperties;
    @InjectMocks
    private KafkaMessagingService kafkaMessagingService;
    private KafkaMessageDto kafkaMessageDto;
    private final String TOPIC = "test-topic";

    @BeforeEach
    public void setUp() {
        String email = "email";
        String title = "title";
        String body = "body";
        kafkaMessageDto = new KafkaMessageDto(email, title, body);
        when(kafkaProperties.topicName()).thenReturn(TOPIC);
    }

    @Test
    public void whenSendKafkaMessageThenOk() {
        CompletableFuture future =
                CompletableFuture.completedFuture(mock(SendResult.class));
        when(kafkaTemplate.send(anyString(), any(KafkaMessageDto.class))).thenReturn(future);
        kafkaMessagingService.send(kafkaMessageDto);
        verify(kafkaTemplate).send(TOPIC, kafkaMessageDto);
    }

    @Test
    public void whenSendKafkaMessageThenFailed() {
        CompletableFuture<SendResult<String, KafkaMessageDto>> future =
                CompletableFuture.failedFuture(new  RuntimeException());
        when(kafkaTemplate.send(anyString(), any(KafkaMessageDto.class))).thenReturn(future);
        kafkaMessagingService.send(kafkaMessageDto);
        verify(kafkaTemplate).send(eq(TOPIC), eq(kafkaMessageDto));
    }
}