package com.todo.app.backend.exception;

import com.todo.app.backend.dto.ExceptionDto;
import com.todo.app.backend.exception.task.DeleteTaskException;
import com.todo.app.backend.exception.task.TaskAlreadyExists;
import com.todo.app.backend.exception.task.TaskNotFoundException;
import com.todo.app.backend.exception.task.UpdateTaskException;
import com.todo.app.backend.exception.user.UserAlreadyExists;
import com.todo.app.backend.exception.user.UserNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleDataIntegrityViolationException() {
        String message = "Exception during save new task";
        log.debug(message);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ExceptionDto(message));
    }

    @ExceptionHandler(UpdateTaskException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleUpdateTaskException() {
        String message = "Exception during update task";
        log.debug(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDto(message));
    }

    @ExceptionHandler(DeleteTaskException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleDeleteTaskException() {
        String message = "Exception during delete task";
        log.debug(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDto(message));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleTaskNotFoundException() {
        String message = "Task not found";
        log.debug(message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDto(message));
    }

    @ExceptionHandler(TaskAlreadyExists.class)
    public ResponseEntity<@NonNull ExceptionDto> handleTaskAlreadyExists() {
        String message = "Task already exists";
        log.debug(message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDto(message));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<@NonNull ExceptionDto> handleUserNotFoundException() {
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
    public ResponseEntity<@NonNull ExceptionDto> handleUserAlreadyExists() {
        String message = "User already exists";
        log.debug(message);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ExceptionDto(message));
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
