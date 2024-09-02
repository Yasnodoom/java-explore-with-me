package ru.practicum.explore.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByUserIdIn(List<Long> ids, Pageable pageable);

    @Query(value = "select * from users", nativeQuery = true)
    List<User> findAllUsers(Pageable pageable);
}
