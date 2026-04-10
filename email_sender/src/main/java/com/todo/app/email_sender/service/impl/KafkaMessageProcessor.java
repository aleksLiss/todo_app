package com.todo.app.email_sender.service.impl;

import com.todo.app.email_sender.model.MessageReceivedEvent;
import com.todo.app.email_sender.service.CreatorService;
import com.todo.app.email_sender.service.MessageProcessor;
import com.todo.app.email_sender.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageProcessor implements MessageProcessor {

    private final CreatorService creatorService;
    private final SenderService senderService;

    @EventListener
    @Override
    public void processMessage(MessageReceivedEvent event) {
        SimpleMailMessage message = creatorService.create(event.dto());
        senderService.sendEmail(message);
    }
}
