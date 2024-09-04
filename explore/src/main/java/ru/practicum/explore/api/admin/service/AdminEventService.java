package ru.practicum.explore.api.admin.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.stat.StatDataService;
import ru.practicum.explore.stat.StatService;
import ru.practicum.explore.storage.EventRepository;
import ru.practicum.explore.storage.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.dto.enums.RequestStatus.CONFIRMED;
import static ru.practicum.explore.utils.EventUtils.updateStatusByAdmin;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    private final StatDataService statDataService;
    private final StatService statService;

    public Event findEventById(Long eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));
    }

    @Transactional
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
        statService.logRequest(request);

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
}
