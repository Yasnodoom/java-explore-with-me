package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
