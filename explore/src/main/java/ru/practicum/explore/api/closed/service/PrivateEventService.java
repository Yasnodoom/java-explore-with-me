package ru.practicum.explore.api.closed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.enums.RequestStatus;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.mapper.RequestMapper;
import ru.practicum.explore.api.admin.service.AdminCategoryService;
import ru.practicum.explore.api.admin.service.AdminUserService;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.storage.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.dto.enums.RequestStatus.*;
import static ru.practicum.dto.event.Status.PUBLISHED;
import static ru.practicum.dto.event.mapper.EventMapper.toEvent;
import static ru.practicum.explore.utils.EventUtils.updateStatusByUser;

@Service
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final AdminUserService adminUserService;
    private final AdminCategoryService adminCategoryService;
    private final PrivateRequestService privateRequestService;

    public Event create(long userId, NewEventDto data) {
        Event event = toEvent(data);
        event.setCategory(adminCategoryService.findById(data.getCategoryId()));
        event.setInitiator(adminUserService.getUserById(userId));

        return eventRepository.save(event);
    }

    public List<Event> findEvents(long userId, int from, int size) {
        return eventRepository.findAllByInitiatorUserId(userId, PageRequest.of(from, size));
    }

    public Event findEvent(long userId, long eventId) {
        return eventRepository
                .findByInitiatorUserIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException(eventId));
    }

    public Event updateEvent(long userId, long eventId, UpdateEventUserRequest data) {
        Event event = findEvent(userId, eventId);

        if (event.getState().equals(PUBLISHED)) {
            throw new ValidationException("not published");
        }
        if (data.getEventDate() != null
                && data.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("not published");
        }

        updateStatusByUser(event, data.getStateAction());
        event.update(toEvent(data));

        return eventRepository.save(event);
    }

    public List<ParticipationRequestDto> findRequests(long userId, long eventId) {
        return privateRequestService
                .findByRequesterAndEvent(userId, eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    public List<ParticipationRequestDto> findRequestsOnUserEvent(long eventId) {
        return privateRequestService
                .findAllByEventId(eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    public Boolean isParticipantLimitEmpty(Event event) {
        return event.getParticipantLimit()
                <= privateRequestService.getCountRequestByEventAndStatus(event.getId(), CONFIRMED);
    }

    public EventRequestStatusUpdateResult updateRequest(
            long userId,
            long eventId,
            EventRequestStatusUpdateRequest data) {

        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder().build();
        Event event = findEvent(userId, eventId);
        RequestStatus status = data.getStatus();
        List<ParticipationRequestDto> requests = findRequestsOnUserEvent(eventId)
//                findRequests(userId, eventId)
                .stream()
                .filter(request -> data.getRequestIds().contains(request.getId()))
                .toList();

        if (isParticipantLimitEmpty(event) && status.equals(CONFIRMED)) {
            throw new ValidationException("limit empty");
        }

        if (event.getRequestModeration().equals(false) || isParticipantLimitEmpty(event)) {
            if (status.equals(REJECTED)) {
                requests.forEach(request -> {
                    request.setStatus(status);
                    result.getRejectedRequests().add(request);
                });
                return result;
            }
        }

        if (!isParticipantLimitEmpty(event) && status.equals(CONFIRMED)) {
            for (ParticipationRequestDto request : requests) {
                if (!request.getStatus().equals(PENDING)) {
                    throw new ValidationException("only pending");
                }
                request.setStatus(status);
                result.getConfirmedRequests().add(request);
                if (isParticipantLimitEmpty(event)) {
                    request.setStatus(REJECTED);
                    result.getRejectedRequests().add(request);
                }
            }
        }
        return result;
    }
}
