package ru.practicum.dto.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto
                .builder()
                .created(request.getCreated())
                .eventId(request.getEvent().getId())
                .id(request.getId())
                .requesterId(request.getRequester().getUserId())
                .status(request.getStatus())
                .build();
    }
}
