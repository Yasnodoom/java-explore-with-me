package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;

    @JsonProperty("event")
    private Long eventId;

    @JsonProperty("requester")
    private Long requesterId;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
