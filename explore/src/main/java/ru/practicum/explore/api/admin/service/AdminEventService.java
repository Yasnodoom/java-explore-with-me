package ru.practicum.explore.api.admin.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.complaint.Complaint;
import ru.practicum.dto.enums.ComplaintStatus;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.explore.api.closed.service.CommentService;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.stat.StatDataService;
import ru.practicum.explore.storage.ComplaintRepository;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.dto.enums.ComplaintStatus.NEW;
import static ru.practicum.dto.enums.ComplaintStatus.VERIFIED;
import static ru.practicum.dto.enums.RequestStatus.CONFIRMED;
import static ru.practicum.explore.utils.EventUtils.updateStatusByAdmin;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final ComplaintRepository complaintRepository;

    private final StatDataService statDataService;
    private final CommentService commentService;

    public Event findEventById(Long eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));
    }

    public List<EventFullDto> findAll(HttpServletRequest request,
                                      List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Integer from,
                                      Integer size) {
        List<Event> events = eventRepository.findAllByParams(users, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from, size));

        if (categories != null) {
            events.removeIf(el -> !categories.contains(el.getCategory().getId()));
        }

        List<EventFullDto> eventsFullDto = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .toList();

        eventsFullDto.forEach(e -> e.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(e.getId(), CONFIRMED)));
        eventsFullDto.forEach(e -> e.setViews(statDataService.getRequestHits(request.getRequestURI())));
        eventsFullDto.forEach(e -> e.setComments(commentService.getAllCommentsByEventId(e.getId())));

        statDataService.logRequest(request);

        return eventsFullDto;
    }

    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest data) {
        Event event = findEventById(eventId);

        updateStatusByAdmin(event, data.getStateAction());
        event.update(EventMapper.toEvent(data));
        EventFullDto fullEventDto = EventMapper.toEventFullDto(eventRepository.save(event));
        fullEventDto.setConfirmedRequests(requestRepository.countByEventIdAndStatus(eventId, CONFIRMED));

        return fullEventDto;
    }

    public Complaint considerComplaint(long complaintId, ComplaintStatus status) {
        Complaint complaint = complaintRepository
                .findById(complaintId)
                .orElseThrow(() -> new NotFoundException(complaintId));

        if (complaint.getStatus() != NEW) {
            throw new ValidationException("can consider only in new status");
        }
        complaint.setStatus(status);

        if (complaint.getStatus().equals(VERIFIED)) {
            long commentId = complaint.getComment().getId();
            complaintRepository.deleteById(complaintId);
            commentService.deleteCommentById(commentId);
            return null;
        }

        return complaintRepository.save(complaint);
    }
}