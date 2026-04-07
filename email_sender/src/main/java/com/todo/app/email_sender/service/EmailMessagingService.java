package com.todo.app.email_sender.service;

import com.todo.app.email_sender.dto.KafkaMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailMessagingService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String from, String to, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(message);
        simpleMailMessage.setTo(to);
        javaMailSender.send(simpleMailMessage);
    }
}
