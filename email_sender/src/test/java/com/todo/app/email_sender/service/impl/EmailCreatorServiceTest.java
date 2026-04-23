package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.config.SmtpProperties;
import com.todo.app.email_sender.dto.KafkaMessageDto;
import com.todo.app.email_sender.mapper.SimpleMailMassageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailCreatorServiceTest {

    @Mock
    private SmtpProperties smtpProperties;
    @Mock
    private SimpleMailMassageMapper simpleMailMassageMapper;
    @InjectMocks
    private EmailCreatorService emailCreatorService;
    private KafkaMessageDto kafkaMessageDto;
    private SimpleMailMessage simpleMailMessage;
    private String emailFrom;

    @BeforeEach
    public void setUp() {
        emailFrom = "aleks@google.com";
        String title = "title";
        String body = "body";
        kafkaMessageDto = new KafkaMessageDto(emailFrom, title, body);
        simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailFrom);
        simpleMailMessage.setText(title);
        simpleMailMessage.setSubject(body);
    }

    @Test
    public void whenCreateSimpleMailMessageThenOk() {
        when(smtpProperties.from()).thenReturn(emailFrom);
        when(simpleMailMassageMapper.toSimpleMailMessage(any(KafkaMessageDto.class)))
                .thenReturn(simpleMailMessage);
        SimpleMailMessage expectedMail = emailCreatorService.create(kafkaMessageDto);
        assertThat(expectedMail).isNotNull();
        assertThat(expectedMail.getFrom()).isEqualTo(emailFrom);
    }
}