package ru.practicum.explore.api.closed.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.explore.api.closed.service.PrivateEventService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateEventController {
    private final PrivateEventService service;

    @PostMapping("/{userId}/events")
    @ResponseStatus(CREATED)
    public Event create(@PathVariable long userId, @Valid @RequestBody final NewEventDto data) {
        return service.create(userId, data);
    }

    @GetMapping("/{userId}/events")
    public List<Event> findEvents(@PathVariable long userId,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return service.findEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public Event findEvent(@PathVariable long userId, @PathVariable long eventId) {
        return service.findEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public Event updateEvent(@PathVariable long userId,
                             @PathVariable long eventId,
                             @Valid @RequestBody final UpdateEventUserRequest data) {
        return service.updateEvent(userId, eventId, data);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequests(
            @PathVariable long eventId) {
        return service.findRequestsOnUserEvent(eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "eventId") long eventId,
            @Valid @RequestBody final EventRequestStatusUpdateRequest data) {
        return service.updateRequest(userId, eventId, data);
    }
}
