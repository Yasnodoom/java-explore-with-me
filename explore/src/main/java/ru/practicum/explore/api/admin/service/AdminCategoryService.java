package ru.practicum.explore.api.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.Category;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.CategoryRepository;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository repository;

    public Category save(Category data) {
        return repository.save(data);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Category patch(long id, Category data) {
        Category category = findById(id);
        category.setName(data.getName());

        return repository.save(category);
    }

    public Category findById(long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }
}
