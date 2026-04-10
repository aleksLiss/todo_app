package com.todo.app.email_sender.service;

import com.todo.app.email_sender.model.MessageReceivedEvent;

public interface MessageProcessor {

    void processMessage(MessageReceivedEvent event);
}
