package ru.practicum.explore.api.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilation.Compilation;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.CompilationRepository;

import java.util.stream.Collectors;

import static ru.practicum.dto.compilation.mapper.CompilationMapper.toCompilation;
import static ru.practicum.dto.compilation.mapper.CompilationMapper.toCompilationDto;

@Service
@RequiredArgsConstructor
public class AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final AdminEventService adminEventService;

    public CompilationDto update(long compId, UpdateCompilationRequest data) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new NotFoundException(compId));

        if (data.getPinned() != null) {
            compilation.setPinned(data.getPinned());
        }
        if (data.getTitle() != null) {
            compilation.setTitle(data.getTitle());
        }
        if (!data.getEvents().isEmpty()) {
            compilation.getEvents().clear();
            compilation.setEvents(data.getEvents()
                    .stream()
                    .map(adminEventService::findEventById)
                    .collect(Collectors.toSet()));
        }

        return toCompilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto create(NewCompilationDto data) {
        Compilation compilation = toCompilation(data);

        for (Long eventId : data.getEvents()) {
            compilation.getEvents().add(adminEventService.findEventById(eventId));
        }

        return toCompilationDto(compilationRepository.save(compilation));
    }

    public void delete(long compId) {
        compilationRepository.deleteById(compId);
    }
}
