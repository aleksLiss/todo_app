package com.todo.app.backend.exception.task;

public class UpdateTaskException extends RuntimeException {
    public UpdateTaskException(String message) {
        super(message);
    }

    public UpdateTaskException() {}
}
