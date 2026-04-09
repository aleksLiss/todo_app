package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService implements SenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(SimpleMailMessage message) {
        javaMailSender.send(message);
    }
}
