package com.todo.app.backend.exception.task;

public class TaskAlreadyExists extends RuntimeException {
    public TaskAlreadyExists(String message) {
        super(message);
    }

    public TaskAlreadyExists() {
    }
}
