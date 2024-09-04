package ru.practicum.server.handler;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
