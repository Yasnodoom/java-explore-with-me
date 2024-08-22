package ru.practicum.server.sevice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.ViewStats;
import ru.practicum.server.storage.ServerRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final ServerRepository repository;

    public Event save(Event data) {
        return repository.save(data);
    }

    public List<ViewStats> findByParams(Timestamp start, Timestamp end, List<String> uris, boolean unique) {
        if (unique) {
            return repository.findByParamsUniqueIp(start, end, uris);
        } else {
            return repository.findByParams(start, end, uris);
        }
    }

}
