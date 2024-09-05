package ru.practicum.server.sevice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.logevent.LogEvent;
import ru.practicum.dto.logevent.ViewStats;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.storage.ServerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final ServerRepository repository;

    public LogEvent save(LogEvent data) {
        return repository.save(data);
    }

    public List<ViewStats> findByParams(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start == null || end == null) {
            throw new ValidationException("date is null");
        }
        if (start.isAfter(end)) {
            throw new ValidationException("start date is after end date ");
        }

        if (unique) {
            return repository.findByParamsUniqueIp(start, end, uris);
        } else {
            return repository.findByParams(start, end, uris);
        }
    }

}
