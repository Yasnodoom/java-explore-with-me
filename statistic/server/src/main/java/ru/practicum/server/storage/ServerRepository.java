package ru.practicum.server.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.logevent.LogEvent;
import ru.practicum.dto.logevent.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<LogEvent, Long> {
    @Query(value = """
            SELECT e.app, e.uri, COUNT(e.ip) AS hits
            FROM log_events e
            WHERE e.timestamp BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp)
            AND (:uris is null or e.uri IN (:uris))
            GROUP BY e.uri, e.app
            ORDER BY hits DESC""", nativeQuery = true)
    List<ViewStats> findByParams(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 @Param("uris") List<String> uris);

    @Query(value = """
            SELECT e.app, e.uri, COUNT(distinct e.ip) AS hits
            FROM log_events e
            WHERE e.timestamp BETWEEN cast(:start AS timestamp) AND cast(:end AS timestamp)
            AND coalesce(:uris, null) is null or e.uri IN (:uris)
            GROUP BY e.uri, e.app
            ORDER BY hits DESC""", nativeQuery = true)
    List<ViewStats> findByParamsUniqueIp(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end,
                                         @Param("uris") List<String> uris);
}
