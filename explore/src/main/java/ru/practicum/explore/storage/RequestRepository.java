package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.enums.RequestStatus;
import ru.practicum.dto.request.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterUserIdAndEventId(long userId, long eventId);

    Integer countByEventIdAndStatus(long eventId, RequestStatus status);

    List<Request> findAllByRequesterUserId(long userId);

    List<Request> findAllByEventId(long eventId);
}
