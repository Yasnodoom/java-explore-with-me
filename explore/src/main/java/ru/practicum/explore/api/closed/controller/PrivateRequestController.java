package ru.practicum.explore.api.closed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.explore.api.closed.service.PrivateRequestService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateRequestController {
    private final PrivateRequestService privateRequestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        return privateRequestService.create(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findAllByUserId(@PathVariable Long userId) {
        return privateRequestService.findAllByUserId(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return privateRequestService.cancel(userId, requestId);
    }

}
