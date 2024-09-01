package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import ru.practicum.dto.enums.UserStateAction;
import ru.practicum.dto.location.Location;

import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest {
    private String annotation;
    private Long categoryId;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private UserStateAction stateAction;
    private String title;
}

