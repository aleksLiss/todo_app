package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import com.todo.app.email_sender.exception.SendEmailException;
import com.todo.app.email_sender.model.MessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private ApplicationEventPublisher publisher;
    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;
    private KafkaMessageDto kafkaMessageDto;

    @BeforeEach
    public void setUp() {
        String email = "email";
        String title = "title";
        String body = "body";
        kafkaMessageDto = new KafkaMessageDto(email, title, body);
    }

    @Test
    public void whenMessageFailedReceivedThenThrow() {
        doThrow(new RuntimeException("Test exception")).when(publisher).publishEvent(any(MessageReceivedEvent.class));
        SendEmailException sendEmailException = assertThrows(SendEmailException.class, ()
                -> kafkaConsumerService.receive(kafkaMessageDto));
        assertEquals("Exception during publish event", sendEmailException.getMessage());
        verify(publisher).publishEvent(any(MessageReceivedEvent.class));
    }

    @Test
    public void whenMessageReceivedThenOk() {
        doNothing().when(publisher).publishEvent(any(MessageReceivedEvent.class));
        kafkaConsumerService.receive(kafkaMessageDto);
        verify(publisher).publishEvent(any(MessageReceivedEvent.class));
    }
}