package ru.practicum.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.enums.RequestStatus;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
