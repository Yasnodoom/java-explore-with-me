package ru.practicum.explore.api.open.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.enums.SortType;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShotDto;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.dto.statdata.StatData;
import ru.practicum.explore.api.admin.service.AdminEventService;
import ru.practicum.explore.api.closed.service.PrivateEventService;
import ru.practicum.explore.api.closed.service.PrivateRequestService;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.stat.StatDataService;
import ru.practicum.explore.stat.StatService;
import ru.practicum.explore.storage.EventRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static ru.practicum.dto.enums.RequestStatus.CONFIRMED;
import static ru.practicum.dto.event.Status.PUBLISHED;
import static ru.practicum.dto.event.mapper.EventMapper.toEventFullDto;

@Service
@RequiredArgsConstructor
public class PublicEventService {
    private final EventRepository eventRepository;
    private final AdminEventService adminEventService;
    private final PrivateRequestService privateRequestService;
    private final PrivateEventService privateEventService;

    private final StatService statService;
    private final StatDataService statDataService;

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


        // fucki
//        eventShotDtoList.forEach(el -> el.setViews(statDataService.getRequestHits(request.getRequestURI())));


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", rangeStart);
        parameters.put("end", rangeEnd);
        parameters.put("unique", false);
        parameters.put("uris", request.getRequestURI());
        List<StatData> stat = statDataService.getStat(parameters);

        if (stat.isEmpty()) {
            eventShotDtoList.forEach(el -> el.setViews(0));
        } else {
            eventShotDtoList.forEach(el -> el.setViews(stat.get(0).getHits()));
        }


        statService.logRequest(request);

        return eventShotDtoList;
    }

    public EventFullDto getById(HttpServletRequest request, Long id) {
        Event event = adminEventService.findEventById(id);

        if (!event.getState().equals(PUBLISHED)) {
            throw new NotFoundException(event.getId());
        }

        int confirmRequests = privateRequestService.getCountRequestByEventAndStatus(event.getId(), CONFIRMED);

        EventFullDto eventFullDto = toEventFullDto(event);
        eventFullDto.setConfirmedRequests(confirmRequests);



        eventFullDto.setViews(statDataService.getRequestHits(request.getRequestURI()));

//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("start", LocalDateTime.now().minusMonths(1));
//        parameters.put("end", LocalDateTime.now().plusMonths(1));
//        parameters.put("unique", true);
//        parameters.put("uris", request.getRequestURI());
//        List<StatData> stat = fuckinService.getStat(parameters);
//
//        if (stat.isEmpty()) {
//            eventFullDto.setViews(0);
//        } else {
//            eventFullDto.setViews(stat.get(0).getHits());
//        }
        statService.logRequest(request);

        return eventFullDto;
    }



}
