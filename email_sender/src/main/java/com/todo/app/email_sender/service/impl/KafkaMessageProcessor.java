package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.exception.SendEmailException;
import com.todo.app.email_sender.model.MessageReceivedEvent;
import com.todo.app.email_sender.service.CreatorService;
import com.todo.app.email_sender.service.MessageProcessor;
import com.todo.app.email_sender.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProcessor implements MessageProcessor {

    private final CreatorService creatorService;
    private final SenderService senderService;

    @EventListener
    @Override
    public void processMessage(MessageReceivedEvent event) {
        SimpleMailMessage message = creatorService.create(event.dto());
        try {
            senderService.sendEmail(message);
        } catch (Exception e) {
            throw new SendEmailException("Exception during process email message");
        }
    }
}