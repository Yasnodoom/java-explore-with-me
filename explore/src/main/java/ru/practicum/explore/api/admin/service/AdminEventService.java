package ru.practicum.explore.api.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.exception.ValidationException;
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

    public Event findEventById(Long eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));
    }

    public List<EventFullDto> findAll(List<Long> users,
                                      List<String> states,
                                      List<Long> categories,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Integer from,
                                      Integer size) {
        List<Event> events;
        // cast
        if (users == null && states == null) {
            events = eventRepository.findAllEvents(PageRequest.of(from, size));
        } else {
            events = eventRepository
                    .findAllByInitiatorUserIdInAndStateInAndCategoryIdInAndEventDateBetween(
                            users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size));
        }

        List<EventFullDto> eventsFullDto = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .toList();

        eventsFullDto.forEach(e -> e.setViews(777));
        eventsFullDto.forEach(e -> e.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(e.getId(), CONFIRMED)));

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

    private void validateData(UpdateEventAdminRequest data) {
        if (data.getEventDate() != null && data.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("date " + data.getEventDate() + " has already arrived");
        }
    }
}
