package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.exception.SendEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailSenderService emailSenderService;
    private SimpleMailMessage simpleMailMessage;

    @BeforeEach
    public void setUp() {
        String from = "from";
        String to = "to";
        String text = "test text";
        simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(text);
    }

    @Test
    public void whenSendEmailThenThrowException() {
        doThrow(new SendEmailException("Exception during send email"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));
        SendEmailException exception = assertThrows(SendEmailException.class, ()
                -> javaMailSender.send(simpleMailMessage));
        assertThat(exception.getMessage()).isEqualTo("Exception during send email");
        }

    @Test
    public void whenSendEmailThenOk() {
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        javaMailSender.send(simpleMailMessage);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}