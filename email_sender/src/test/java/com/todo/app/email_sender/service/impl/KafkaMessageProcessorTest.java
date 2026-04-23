package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import com.todo.app.email_sender.exception.SendEmailException;
import com.todo.app.email_sender.model.MessageReceivedEvent;
import com.todo.app.email_sender.service.CreatorService;
import com.todo.app.email_sender.service.SenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageProcessorTest {

    @Mock
    private CreatorService creatorService;
    @Mock
    private SenderService senderService;
    @InjectMocks
    private KafkaMessageProcessor kafkaMessageProcessor;
    private SimpleMailMessage simpleMailMessage;
    private KafkaMessageDto kafkaMessageDto;
    private MessageReceivedEvent event;

    @BeforeEach
    public void setUp() {
        kafkaMessageDto = new KafkaMessageDto("email", "title", "body");
        simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(kafkaMessageDto.email());
        simpleMailMessage.setSubject(kafkaMessageDto.title());
        simpleMailMessage.setText(kafkaMessageDto.body());
        event = new MessageReceivedEvent(kafkaMessageDto);
    }

    @Test
    public void whenProcessMessageThenOk() {
        when(creatorService.create(any(KafkaMessageDto.class))).thenReturn(simpleMailMessage);
        doNothing().when(senderService).sendEmail(any(SimpleMailMessage.class));
        SimpleMailMessage expectedMessage = creatorService.create(kafkaMessageDto);
        assertThat(expectedMessage.getFrom()).isEqualTo(kafkaMessageDto.email());
        senderService.sendEmail(simpleMailMessage);
        verify(senderService).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    public void whenProccesMessageThenThrowException() {
        when(creatorService.create(any(KafkaMessageDto.class))).thenReturn(simpleMailMessage);
        doThrow(new SendEmailException("Exception during process email message"))
                .when(senderService).sendEmail(any(SimpleMailMessage.class));
        SimpleMailMessage expectedMessage = creatorService.create(kafkaMessageDto);
        assertThat(expectedMessage.getFrom()).isEqualTo(kafkaMessageDto.email());
        assertThatThrownBy(() -> kafkaMessageProcessor.processMessage(event))
                .isInstanceOf(SendEmailException.class)
                .hasMessage("Exception during process email message");
    }
}