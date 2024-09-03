package ru.practicum.explore.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.event.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorUserId(Long userId, Pageable pageable);

    @Query(value = """
            select * from events e
            where coalesce(:userIds, null) is null or e.initiator_id in (:userIds)
            and coalesce(:states, null) is null or e.state in (:states)
            and coalesce(:categories, null) is null or e.category_id in (:categories)
            and  coalesce(:rangeStart, null) is null or (e.event_date between :rangeStart and :rangeEnd)
            order by e.event_date desc
            """, nativeQuery = true)
    List<Event> findAllByParams(@Param("userIds") List<Long> usersIds,
                                @Param("states") List<String> states,
                                @Param("categories") List<Long> categories,
                                @Param("rangeStart") LocalDateTime rangeStart,
                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                Pageable pageable);

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
            where :annotation is null or lower(e.annotation) like lower(:annotation)
            and e.state like 'PUBLISHED'
            and :categories is null or e.category_id in :categories
            and :paid is null or e.paid = :paid
            and coalesce(:rangeStart, null) is null or (e.event_date between :rangeStart and :rangeEnd)
            """, nativeQuery = true)
    List<Event> findByParams(@Param("annotation") String annotation,
                             @Param("categories") List<Long> categories,
                             @Param("paid") Boolean paid,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd,
                             Pageable pageable);

    @Query(value = """
            select * from events e
            where :annotation is null or lower(e.annotation) like lower(:annotation)
            and e.state like 'PUBLISHED'
            and :categories is null or e.category_id in :categories
            and :paid is null or e.paid = :paid
            and e.event_date > current_timestamp
            """, nativeQuery = true)
    List<Event> findByParamsWithoutTimeRage(@Param("annotation") String annotation,
                                            @Param("categories") List<Long> categories,
                                            @Param("paid") Boolean paid,
                                            Pageable pageable);

    List<Event> findAllByCompilationsId(long compId);

    // cast
    @Query(value = "select * from events e", nativeQuery = true)
    List<Event> findAllEvents(Pageable pageable);
}
