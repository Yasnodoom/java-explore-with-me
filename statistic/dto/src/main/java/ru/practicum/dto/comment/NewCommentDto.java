package ru.practicum.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCommentDto {
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(max = 5000)
    private String text;

    @NotNull
    private Long eventId;
}
