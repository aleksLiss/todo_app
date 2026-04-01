package com.todo.app.backend.exception;

public class KafkaSendMessageException extends RuntimeException {
    public KafkaSendMessageException(String message) {
        super(message);
    }

    public KafkaSendMessageException() {
    }
}
