package com.todo.app.backend.dto;

import java.util.UUID;

public record GetUserResponseDto(
        UUID id,
        String email
) {
}
