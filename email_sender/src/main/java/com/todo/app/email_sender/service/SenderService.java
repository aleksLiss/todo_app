package com.todo.app.email_sender.service;

import org.springframework.mail.SimpleMailMessage;

public interface SenderService {

    void sendEmail(SimpleMailMessage message);
}
