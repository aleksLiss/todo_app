package com.todo.app.backend.exception.task;

public class DeleteTaskException extends RuntimeException {
    public DeleteTaskException(String message) {
        super(message);
    }

    public DeleteTaskException() {
    }
}
