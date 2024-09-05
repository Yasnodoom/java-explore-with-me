package ru.practicum.explore.api.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explore.api.admin.service.AdminCompilationService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable long compId,
                                 @Valid @RequestBody final UpdateCompilationRequest data) {
        return adminCompilationService.update(compId, data);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CompilationDto create(@Valid @RequestBody final NewCompilationDto data) {
        return adminCompilationService.create(data);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void delete(@PathVariable long compId) {
        adminCompilationService.delete(compId);
    }
}
