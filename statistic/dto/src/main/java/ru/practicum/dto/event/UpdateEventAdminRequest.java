package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import ru.practicum.dto.location.Location;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {
    private String annotation;
    private String description;
    private Long categoryId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit;
    private Boolean requestModeration = false;

    @Enumerated(EnumType.STRING)
    private StateActionEvent stateAction;
    private String title;
}

