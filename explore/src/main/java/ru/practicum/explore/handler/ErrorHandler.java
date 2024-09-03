package ru.practicum.explore.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;

import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(final NotFoundException e) {
        final ApiError error = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).toList())
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.NOT_FOUND.name())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(final ValidationException e) {
        final ApiError error = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).toList())
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleValidationException(final ConflictException e) {
        final ApiError error = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).toList())
                .message(e.getMessage())
                .reason(e.getMessage())
                .status(HttpStatus.CONFLICT.name())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(final ConstraintViolationException e) {
        final ApiError error = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).toList())
                .message(e.getMessage())
                .reason("constraint violation exception")
                .status(HttpStatus.BAD_REQUEST.name())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(final org.hibernate.exception.ConstraintViolationException e) {
        final ApiError error = ApiError.builder()
                .errors(Arrays.stream(e.getStackTrace()).toList())
                .message(e.getMessage())
                .reason("duplicate key value violates")
                .status(HttpStatus.CONFLICT.name())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
