package ru.practicum.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.ViewStats;
import ru.practicum.server.sevice.EventService;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticServerController {
    private final EventService eventService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Event save(@RequestBody final Event event) {
        return eventService.save(event);
    }

    @SneakyThrows
    @GetMapping("/stats")
    public List<ViewStats> stats(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(required = false, defaultValue = "false") boolean unique) {
        Timestamp s = Timestamp.valueOf(start);
        Timestamp e = Timestamp.valueOf(end);
        uris = uris == null ?  Collections.emptyList() : uris;

        return eventService.findByParams(s, e, uris, unique);
    }
}
