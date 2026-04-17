package com.todo.app.email_sender.exception;

import com.todo.app.email_sender.dto.ExceptionDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleSendEmailException(SendEmailException ex) {
        String message = "Email was not sent";
        log.warn("=============Exception message: " + ex.getMessage());
        log.debug(message);
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ExceptionDto(message));
    }

    @ExceptionHandler(ListenerExecutionFailedException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleListenerException(ListenerExecutionFailedException ex) {
        log.warn("=============Exception message: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ExceptionDto(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ExceptionDto> handleException() {
        String message = "Internal Server Error";
        log.debug(message);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDto(message));
    }
}