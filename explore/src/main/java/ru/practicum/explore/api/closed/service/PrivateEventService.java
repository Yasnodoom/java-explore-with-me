package ru.practicum.explore.api.closed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.Comment;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.dto.comment.mapper.CommentMapper;
import ru.practicum.dto.enums.RequestStatus;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.Request;
import ru.practicum.dto.request.mapper.RequestMapper;
import ru.practicum.explore.api.admin.service.AdminCategoryService;
import ru.practicum.explore.api.admin.service.AdminUserService;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.storage.CommentRepository;
import ru.practicum.explore.storage.ComplaintRepository;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.dto.enums.RequestStatus.*;
import static ru.practicum.dto.event.Status.PUBLISHED;
import static ru.practicum.dto.event.mapper.EventMapper.toEvent;
import static ru.practicum.dto.request.mapper.RequestMapper.toParticipationRequestDto;
import static ru.practicum.explore.utils.EventUtils.updateStatusByUser;

@Service
@RequiredArgsConstructor
public class PrivateEventService {
    private final AdminUserService adminUserService;
    private final AdminCategoryService adminCategoryService;
    private final PrivateRequestService privateRequestService;
    private final CommentService commentService;

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private final ComplaintRepository complaintRepository;

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

    public EventFullDto getEvent(long userId, long eventId) {
        EventFullDto eventFullDto = eventRepository
                .findByInitiatorUserIdAndId(userId, eventId)
                .map(EventMapper::toEventFullDto)
                .orElseThrow(() -> new NotFoundException(eventId));

        List<CommentFullDto> comments = commentService.getAllCommentsByEventId(eventFullDto.getId());
        comments.forEach(c -> c.setComplaints(complaintRepository.findAllByCommentId(c.getId())));
        eventFullDto.setComments(comments);

        return eventFullDto;
    }

    public Event updateEvent(long userId, long eventId, UpdateEventUserRequest data) {
        Event event = findEvent(userId, eventId);

        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException("not published");
        }
        if (data.getEventDate() != null
                && data.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("not published");
        }

        updateStatusByUser(event, data.getStateAction());
        event.update(toEvent(data));

        return eventRepository.save(event);
    }

    public List<ParticipationRequestDto> findRequestsOnUserEvent(long eventId) {
        return privateRequestService
                .findAllByEventId(eventId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .toList();
    }

    public Boolean isParticipantLimitIsEmpty(Event event) {
        return event.getParticipantLimit()
                <= privateRequestService.getCountRequestByEventAndStatus(event.getId(), CONFIRMED);
    }

    public EventRequestStatusUpdateResult updateRequest(long userId, long eventId,
                                                        EventRequestStatusUpdateRequest data) {
        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder().build();
        Event event = findEvent(userId, eventId);
        RequestStatus newRequestStatus = data.getStatus();

        List<Request> requests = requestRepository.findAllByEventId(eventId)
                .stream()
                .filter(request -> data.getRequestIds().contains(request.getId()))
                .toList();

        if (event.getParticipantLimit() == 0 || event.getRequestModeration().equals(false)) {
            return result;
        }

        if (isParticipantLimitIsEmpty(event)) {
            throw new ConflictException("limit empty");
        }

        for (Request r : requests) {
            if (!r.getStatus().equals(PENDING)) {
                throw new ConflictException("change status can on only in pending");
            }
            if (isParticipantLimitIsEmpty(event)) {
                r.setStatus(REJECTED);
                requestRepository.save(r);
                result.getRejectedRequests().add(toParticipationRequestDto(r));
            }

            r.setStatus(newRequestStatus);
            requestRepository.save(r);

            if (newRequestStatus.equals(REJECTED)) {
                result.getRejectedRequests().add(toParticipationRequestDto(r));
            } else {
                result.getConfirmedRequests().add(toParticipationRequestDto(r));
            }
        }

        return result;
    }

    public CommentFullDto getComment(long userId, long commentId) {
        checkUserPermission(userId, commentId);

        CommentFullDto commentFullDto = commentRepository
                .findById(commentId)
                .map(CommentMapper::toFullDto)
                .orElseThrow(() -> new NotFoundException(commentId));
        commentFullDto.setComplaints(complaintRepository.findAllByCommentId(commentId));

        return commentFullDto;
    }

    public CommentFullDto updateComment(long userId, long commentId, UpdateCommentDto data) {
        checkUserPermission(userId, commentId);

        Comment comment = commentService.getCommentById(commentId);
        comment.setText(data.getText());

        return CommentMapper.toFullDto(commentRepository.save(comment));
    }

    public void deleteComment(long userId, long commentId) {
        checkUserPermission(userId, commentId);
        commentRepository.deleteById(commentId);
    }

    public CommentFullDto createComment(long userId, NewCommentDto data) {
        Comment comment = CommentMapper.toComment(data);
        Event event = eventRepository
                .findById(data.getEventId())
                .orElseThrow(() -> new NotFoundException(data.getEventId()));

        if (userId == event.getInitiator().getUserId()) {
            throw new ValidationException("forbidden to comment on your own events");
        }

        comment.setAuthor(adminUserService.getUserById(userId));
        comment.setEvent(event);

        return CommentMapper.toFullDto(commentRepository.save(comment));
    }

    private void checkUserPermission(long userId, long commentId) {
        Comment comment = commentService.getCommentById(commentId);

        if (comment.getAuthor().getUserId() != userId) {
            throw new ValidationException("not allowed");
        }
    }
}
