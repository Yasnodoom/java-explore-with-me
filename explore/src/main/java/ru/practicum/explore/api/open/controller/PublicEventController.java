package ru.practicum.explore.api.open.controller;

import jakarta.servlet.http.HttpServletRequest;
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


    @GetMapping
    public List<EventShotDto> find(HttpServletRequest request,
                                   @RequestParam String text,
                                   @RequestParam List<Long> categories,
                                   @RequestParam Boolean paid,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime rangeStart,
                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime rangeEnd,
                                   @RequestParam Boolean onlyAvailable,
                                   @RequestParam SortType sort,
                                   @RequestParam(defaultValue = "0") Integer from,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.find(request, text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getById(HttpServletRequest request, @PathVariable Long id) {
        return publicEventService.getById(request, id);
    }
}
