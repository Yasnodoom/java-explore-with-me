package ru.practicum.explore.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.event.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorUserId(Long userId, Pageable pageable);

    List<Event> findAllByInitiatorUserIdInAndStateInAndCategoryIdInAndEventDateBetween(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Pageable pageable);

    Optional<Event> findByInitiatorUserIdAndId(long userId, long eventId);

    @Query(value = """
            select * from events e
            where lower(e.annotation) like lower(?1)
            and e.state like 'PUBLISHED'
            and e.category_id in ?2
            and e.paid = ?3
            and e.event_date between ?4 and ?5
            """, nativeQuery = true)
    List<Event> findByParams(String text,
                             List<Long> categories,
                             Boolean paid,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                             Pageable pageable);

    @Query(value = """
            select * from events e
            where lower(e.annotation) like lower(?1)
            and e.state like 'PUBLISHED'
            and e.category_id in ?2
            and e.paid = ?3
            and e.event_date > current_timestamp
            """, nativeQuery = true)
    List<Event> findByParamsWithoutTimeRage(String text, List<Long> categories, Boolean paid,
                                            Pageable pageable);

    List<Event> findAllByCompilationsId(long compId);
}
