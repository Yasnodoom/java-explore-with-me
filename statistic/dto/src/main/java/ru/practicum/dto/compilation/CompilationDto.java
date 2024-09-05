package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.EventShotDto;

import java.util.Set;

@Data
@Builder
public class CompilationDto {
    private Long id;
    private Set<EventShotDto> events;
    private Boolean pinned;
    private String title;
}
