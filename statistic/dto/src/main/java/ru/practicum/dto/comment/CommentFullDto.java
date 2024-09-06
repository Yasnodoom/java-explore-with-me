package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.complaint.ComplaintIdTextDto;
import ru.practicum.dto.event.EventShotDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentFullDto {
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max = 5000)
    private String text;

    private EventShotDto event;

    private UserShortDto author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    private List<ComplaintIdTextDto> complaints;
}
