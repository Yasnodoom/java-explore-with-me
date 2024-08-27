package ru.practicum.client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.event.Event;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final BaseClient client;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> hit(@RequestBody final Event event) {
        return client.hit(event);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> stats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) String uris,
                                        @RequestParam(required = false, defaultValue = "false") boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("unique", unique);
        parameters.put("uris", uris);

        return client.stats(parameters);
    }

}
