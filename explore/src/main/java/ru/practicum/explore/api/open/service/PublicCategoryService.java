package ru.practicum.explore.api.open.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.Category;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> find(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size)).toList();
    }

    public Category getById(long camId) {
        return categoryRepository
                .findById(camId)
                .orElseThrow(() -> new NotFoundException(camId));
    }
}
