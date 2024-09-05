package ru.practicum.explore.api.open.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.enums.SortType;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShotDto;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.explore.api.closed.service.PrivateEventService;
import ru.practicum.explore.api.closed.service.PrivateRequestService;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.stat.StatDataService;
import ru.practicum.explore.storage.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import static ru.practicum.dto.enums.RequestStatus.CONFIRMED;
import static ru.practicum.dto.event.Status.PUBLISHED;
import static ru.practicum.dto.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class PublicEventService {
    private final EventRepository eventRepository;
    private final PrivateRequestService privateRequestService;
    private final PrivateEventService privateEventService;
    private final StatDataService statDataService;

    @Transactional
    public List<EventShotDto> find(HttpServletRequest request, String text, List<Long> categories, Boolean paid,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onlyAvailable, SortType sort, Integer from, Integer size) {

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ValidationException("end date is after start date");
            }
        }

        List<EventShotDto> eventShotDtoList;
        List<Event> events;

        if (rangeStart == null || rangeEnd == null) {
            events = eventRepository.findByParamsWithoutTimeRage(text, categories, paid,
                    PageRequest.of(from, size, Sort.by(sort.name()).descending()));
        } else {
            events = eventRepository.findByParams(text, categories, paid, rangeStart,
                    rangeEnd, PageRequest.of(from, size, Sort.by(sort.name()).descending()));
        }

        if (onlyAvailable != null && onlyAvailable) {
            events.removeIf(Predicate.not(privateEventService::isParticipantLimitIsEmpty));
        }

        eventShotDtoList = events
                .stream()
                .map(EventMapper::toEventShotDto)
                .toList();

        eventShotDtoList.forEach(el -> el.setConfirmedRequests(
                privateRequestService.getCountRequestByEventAndStatus(el.getId(), CONFIRMED)));
        eventShotDtoList.forEach(el -> el.setViews(statDataService.getRequestHits(request.getRequestURI())));

        statDataService.logRequest(request);

        return eventShotDtoList;
    }


    public EventFullDto getById(HttpServletRequest request, Long id) {
        Event event = eventRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        if (!event.getState().equals(PUBLISHED)) {
            throw new NotFoundException(event.getId());
        }

        int confirmRequests = privateRequestService.getCountRequestByEventAndStatus(event.getId(), CONFIRMED);

        EventFullDto eventFullDto = toEventFullDto(event);
        eventFullDto.setConfirmedRequests(confirmRequests);
        eventFullDto.setViews(statDataService.getRequestHits(request.getRequestURI()));

        statDataService.logRequest(request);

        return eventFullDto;
    }
}
