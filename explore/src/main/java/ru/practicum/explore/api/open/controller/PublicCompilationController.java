package ru.practicum.explore.api.open.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.explore.api.open.service.PublicCompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> find(@RequestParam(defaultValue = "false") Boolean pinned,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return publicCompilationService.find(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable Long compId) {
        return publicCompilationService.getById(compId);
    }
}
