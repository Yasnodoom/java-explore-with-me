package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.logevent.LogEvent;
import ru.practicum.dto.logevent.ViewStats;
import ru.practicum.server.sevice.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticServerController {
    private final EventService eventService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public LogEvent save(@RequestBody final LogEvent event) {
        return eventService.save(event);
    }

    @SneakyThrows
    @GetMapping("/stats")
    public List<ViewStats> stats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime start,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime end,
                                 @RequestParam(required = false, defaultValue = "") List<String> uris,
                                 @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return eventService.findByParams(start, end, uris, unique);
    }
}
