package com.todo.app.todo_app.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<@NonNull Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors();
        String msg = "Incorrect username or password";
        log.warn(msg);
        String errorMessage = errors.stream()
                .filter(f -> f.getField().equals("username"))
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(errors.getFirst().getDefaultMessage());
        if (errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", msg));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", errorMessage));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<@NonNull Map<String, String>> handleTaskNotFoundException(TaskNotFoundException ex) {
        log.warn("TaskNotFoundException", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(value = {SaveUserException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<@NonNull Map<String, String>> handleException(Exception e) {
        log.warn("Internal Server Error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Internal server error"));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<@NonNull Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.warn("User Already Exists");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<@NonNull Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("User Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<@NonNull Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("Bad Credentials exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Incorrect username or password"));

    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<@NonNull Map<String, String>> handleSignatureException(SignatureException ex) {
        log.warn("JWT Signature error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Token is tampered"));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<@NonNull Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) {
        log.warn("JWT Expired: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Token expired"));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<@NonNull Map<String, String>> handleJwtException(JwtException ex) {
        log.error("JWT Error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Token exception"));
    }

}
