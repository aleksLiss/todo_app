package com.todo.app.scheduler.exception;

public class KafkaSendMessageException extends RuntimeException {
    public KafkaSendMessageException(String message) {
        super(message);
    }

    public KafkaSendMessageException() {
    }
}
