package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.category.Category;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShotDto {
    private String annotation;
    private Category category;
    private Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
