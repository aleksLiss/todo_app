package com.todo.app.email_sender.exception;

public class SendEmailException extends RuntimeException {
    public SendEmailException(String message) {
        super(message);
    }

    public SendEmailException() {
    }
}
