package ru.practicum.explore.handler;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiError {
    private List<StackTraceElement> errors;
    private String message;
    private String reason;
    private String status;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
