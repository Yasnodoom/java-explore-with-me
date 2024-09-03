package ru.practicum.explore.stat;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.dto.logevent.LogEvent;
import ru.practicum.dto.statdata.StatData;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class StatService {
    private final StatClient statClient;

    public void logRequest(HttpServletRequest request) {
        LogEvent log = LogEvent.builder()
                .app(request.getRemoteHost())
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        ResponseEntity<Object> res = statClient.hit(log);

        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("error saving the log");
        }
    }
}
