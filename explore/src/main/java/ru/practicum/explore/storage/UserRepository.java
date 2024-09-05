package ru.practicum.explore.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            select * from users u
            where coalesce(:ids, null) is null or u.user_id in (:ids)
            """, nativeQuery = true)
    List<User> findAllByUserIdIn(@Param("ids") List<Long> ids, Pageable pageable);
}