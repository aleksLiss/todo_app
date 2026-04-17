package com.todo.app.email_sender.service.impl;

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
            log.warn("SENDING EMAIL: " + Arrays.toString(message.getTo()));
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("!!!!!!!! SMTP ERROR: " + e.toString()); // Это покажет реальную причину
            e.printStackTrace();
            throw e;
        }
    }
}