package com.todo.app.email_sender.dto;

public record KafkaMessageDto(
        String email,
        String message
) {
}
