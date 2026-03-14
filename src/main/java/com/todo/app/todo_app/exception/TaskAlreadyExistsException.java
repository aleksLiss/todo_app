package com.todo.app.todo_app.exception;

public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
