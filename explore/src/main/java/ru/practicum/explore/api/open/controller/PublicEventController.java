package ru.practicum.explore.api.open.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.enums.SortType;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShotDto;
import ru.practicum.explore.api.open.service.PublicEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService publicEventService;

    @Transactional
    @GetMapping
    public List<EventShotDto> find(HttpServletRequest request,
                                   @RequestParam(required = false) String text,
                                   @RequestParam(required = false) List<Long> categories,
                                   @RequestParam(required = false) Boolean paid,
                                   @RequestParam(required = false)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                   @RequestParam(required = false)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                   @RequestParam(required = false) Boolean onlyAvailable,
                                   @RequestParam(required = false, defaultValue = "EVENT_DATE") SortType sort,
                                   @RequestParam(defaultValue = "0") Integer from,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.find(request, text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @Transactional
    @GetMapping("/{id}")
    public EventFullDto getById(HttpServletRequest request, @PathVariable Long id) {
        return publicEventService.getById(request, id);
    }
}
