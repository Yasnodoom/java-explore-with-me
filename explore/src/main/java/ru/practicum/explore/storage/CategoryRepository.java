package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
