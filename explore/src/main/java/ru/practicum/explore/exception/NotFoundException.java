package ru.practicum.explore.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long message) {
        super("Not found id: " + message);
    }
}
