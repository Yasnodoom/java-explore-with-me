package ru.practicum.explore.api.closed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.enums.RequestStatus;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.Request;
import ru.practicum.dto.request.mapper.RequestMapper;
import ru.practicum.dto.user.User;
import ru.practicum.explore.api.admin.service.AdminEventService;
import ru.practicum.explore.api.admin.service.AdminUserService;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.storage.RequestRepository;

import java.util.List;

import static ru.practicum.dto.enums.RequestStatus.CANCELED;
import static ru.practicum.dto.enums.RequestStatus.CONFIRMED;
import static ru.practicum.dto.event.Status.PUBLISHED;
import static ru.practicum.dto.request.mapper.RequestMapper.toParticipationRequestDto;

@Service
@RequiredArgsConstructor
public class PrivateRequestService {
    private final RequestRepository requestRepository;
    private final AdminUserService adminUserService;
    private final AdminEventService adminEventService;

    public ParticipationRequestDto create(Long userId, Long eventId) {
        Event event = adminEventService.findEventById(eventId);
        Request request = Request.builder()
                .requester(adminUserService.getUserById(userId))
                .event(event)
                .build();

        validateRequest(request);

        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            request.setStatus(CONFIRMED);
        }

        return toParticipationRequestDto(requestRepository.save(request));
    }

    public List<ParticipationRequestDto> findAllByUserId(long userId) {
        return requestRepository
                .findAllByRequesterUserId(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    public Request findRequestById(long requestId) {
        return requestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException(requestId));
    }

    public ParticipationRequestDto cancel(long userId, long requestId) {
        Request request = findRequestById(requestId);

        if (request.getRequester().getUserId() != userId) {
            throw new ValidationException("not your");
        }
        request.setStatus(CANCELED);

        return toParticipationRequestDto(requestRepository.save(request));
    }


    public List<Request> findAllByEventId(long eventId) {
        return requestRepository.findAllByEventId(eventId);
    }

    public Integer getCountRequestByEventAndStatus(long eventId, RequestStatus status) {
        return requestRepository.countByEventIdAndStatus(eventId, status);
    }

    private void validateRequest(Request request) {
        Event event = request.getEvent();
        User requester = request.getRequester();

        if (requester.equals(event.getInitiator())) {
            throw new ConflictException("same user");
        }
        if (!event.getState().equals(PUBLISHED)) {
            throw new ConflictException("only published");
        }
        if (event.getParticipantLimit() != 0
                && event.getParticipantLimit() <= requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED)) {
            throw new ConflictException("limit empty");
        }

        requestRepository.findByRequesterUserIdAndEventId(requester.getUserId(), event.getId())
                .stream()
                .findAny()
                .ifPresent(s -> {
                    throw new ConflictException("request exist for this user");
                });
    }
}
