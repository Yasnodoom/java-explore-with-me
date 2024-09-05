package ru.practicum.explore.api.open.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.Category;
import ru.practicum.explore.api.open.service.PublicCategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<Category> find(@RequestParam(defaultValue = "0") Integer from,
                               @RequestParam(defaultValue = "10") Integer size) {
        return publicCategoryService.find(from, size);
    }

    @GetMapping("/{catId}")
    public Category getById(@PathVariable Long catId) {
        return publicCategoryService.getById(catId);
    }
}
