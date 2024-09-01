package ru.practicum.explore.api.admin.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore.api.admin.service.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> findAll(@RequestParam List<Long> users,
                                      @RequestParam List<String> states,
                                      @RequestParam List<Long> categories,
                                      @RequestParam
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime rangeStart,
                                      @RequestParam
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return service.findAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @Transactional
    public EventFullDto patch(@PathVariable long eventId,
                              @RequestBody final UpdateEventAdminRequest data) {
        return service.updateEvent(eventId, data);
    }
}
