package com.todo.app.scheduler.exception;

import com.todo.app.scheduler.dto.ExceptionDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KafkaSendMessageException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleKafkaSendMessageException() {
        String exceptionMessage = "Exception occurred during sending Kafka messages";
        log.warn(exceptionMessage);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDto(exceptionMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ExceptionDto> handleException() {
        String exceptionMessage = "Internal Server Error";
        log.warn(exceptionMessage);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDto(exceptionMessage));
    }
}
