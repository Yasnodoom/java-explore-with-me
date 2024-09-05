package ru.practicum.dto.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.*;
import ru.practicum.dto.user.mapper.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
        public static Event toEvent(UpdateEventAdminRequest dto) {
            return Event
                    .builder()
                    .annotation(dto.getAnnotation())
                    // category
                    .description(dto.getDescription())
                    .eventDate(dto.getEventDate())
                    .location(dto.getLocation())
                    .paid(dto.getPaid())
                    .participantLimit(dto.getParticipantLimit())
                    .requestModeration(dto.getRequestModeration())
                    // stateAction
                    .title(dto.getTitle())
                    .build();
        }

        public static Event toEvent(NewEventDto dto) {
            return Event
                    .builder()
                    .annotation(dto.getAnnotation())
                    // category
                    .description(dto.getDescription())
                    .eventDate(dto.getEventDate())
                    .location(dto.getLocation())
                    .paid(dto.getPaid())
                    .participantLimit(dto.getParticipantLimit())
                    .requestModeration(dto.getRequestModeration())
                    .title(dto.getTitle())
                    .build();
        }

    public static Event toEvent(UpdateEventUserRequest dto) {
        return Event
                .builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location(dto.getLocation())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                // stateAction
                .title(dto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto
                .builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                    // confirmedRequests
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                // views
                .build();
    }

    public static EventShotDto toEventShotDto(Event event) {
        return EventShotDto
                .builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                // confirmedRequests
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                // views
                .build();
    }
}

