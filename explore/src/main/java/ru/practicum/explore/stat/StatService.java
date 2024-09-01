package ru.practicum.explore.stat;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.dto.logevent.LogEvent;

import java.time.LocalDateTime;

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
