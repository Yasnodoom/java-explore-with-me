package ru.practicum.explore.api.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.Category;
import ru.practicum.explore.api.admin.service.AdminCategoryService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final AdminCategoryService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public Category create(@Valid @RequestBody final Category data) {
        return service.save(data);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void delete(@PathVariable long catId) {
        service.delete(catId);
    }

    @PatchMapping("/{catId}")
    public Category patch(@PathVariable long catId, @Valid @RequestBody final Category data) {
        return service.patch(catId, data);
    }
}
