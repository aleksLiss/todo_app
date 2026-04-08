package com.todo.app.backend.dto;

public record KafkaMessageDto(
        String email,
        String title,
        String body
) {
}
