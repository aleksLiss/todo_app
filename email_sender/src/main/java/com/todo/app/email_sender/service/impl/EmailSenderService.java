package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.exception.SendEmailException;
import com.todo.app.email_sender.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService implements SenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
        } catch (Exception ex) {
            throw new SendEmailException("Exception during send email");
        }
    }
}