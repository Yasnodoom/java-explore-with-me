package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.practicum.dto.location.Location;

import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 20, max = 2000)
    @Column(name = "annotation")
    private String annotation;

    @JsonProperty("category")
    private Long categoryId;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 20, max = 7000)
    @Column(name = "description")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;
    private Boolean paid = false;

    @Min(value = 0)
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 120)
    private String title;
}
