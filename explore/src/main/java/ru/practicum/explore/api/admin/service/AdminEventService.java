package ru.practicum.explore.api.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explore.utils.EventUtils.updateStatusByAdmin;

@Service
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;

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
        List<Event> events = eventRepository
                .findAllByInitiatorUserIdInAndStateInAndCategoryIdInAndEventDateBetween(
                        users, states, categories, rangeStart, rangeEnd, PageRequest.of(from, size));

        List<EventFullDto> eventsFullDto = events
                .stream()
                .map(EventMapper::toEventFullDto)
                .toList();
        eventsFullDto.forEach(e -> e.setViews(777));

        return eventsFullDto;
    }

    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest data) {
        Event event = findEventById(eventId);

        updateStatusByAdmin(event, data.getStateAction()); //admin
        event.update(EventMapper.toEvent(data));

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }
}
