package com.todo.app.backend.dto.user;

import java.util.UUID;

public record GetUserResponseDto(
        UUID id,
        String email
) {
}
