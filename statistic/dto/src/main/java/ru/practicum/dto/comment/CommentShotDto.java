package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentShotDto {
    private Long id;

    private String text;

    private Long eventId;

    private Long authorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publish;
}
