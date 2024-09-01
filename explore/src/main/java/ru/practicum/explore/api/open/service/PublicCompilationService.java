package ru.practicum.explore.api.open.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.Compilation;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.mapper.CompilationMapper;
import ru.practicum.dto.event.Event;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.CompilationRepository;
import ru.practicum.explore.storage.EventRepository;

import java.util.List;

import static ru.practicum.dto.compilation.mapper.CompilationMapper.toCompilationDto;

@Service
@RequiredArgsConstructor
public class PublicCompilationService {
    private final CompilationRepository compilationRepository;
    //удалить
    private final EventRepository eventRepository;

    public List<CompilationDto> find(Boolean pinned, Integer from, Integer size) {
        return compilationRepository
                .findAllByPinned(pinned, PageRequest.of(from, size))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .toList();
    }

    public CompilationDto getById(long comId) {
        Compilation comp = compilationRepository
                .findById(comId)
                .orElseThrow(() -> new NotFoundException(comId));

        List<Event> events = eventRepository.findAllByCompilationsId(comId);
        if (!events.isEmpty()) {
            for (Event e : events) {
                comp.getEvents().add(e);
            }
        }

        return toCompilationDto(comp);
    }
}
