package com.todo.app.backend.exception;

import com.todo.app.backend.dto.ExceptionDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleUserNotFoundException(UserNotFoundException e) {
        String message = "User not found";
        log.debug(message);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDto(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug("Method argument not valid");
        var errors = ex.getBindingResult().getFieldErrors();
        String errorMessage = errors.stream()
                .filter(f -> f.getField().equals("username"))
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(errors.getFirst().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(errorMessage));
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<@NonNull ExceptionDto> handleUserAlreadyExists(UserAlreadyExists e) {
        String message = "User already exists";
        log.debug(message);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ExceptionDto(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ExceptionDto> handleException(Exception e) {
        String message = "Internal Server Error";
        log.debug(message);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new  ExceptionDto(message));
    }
}
