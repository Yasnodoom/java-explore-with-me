package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.complaint.Complaint;
import ru.practicum.dto.complaint.ComplaintIdTextDto;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query(value = "select c.id, c.text, c.status from complaints c " +
            "where c.comment_id = ?1 " +
            "order by c.create_date desc", nativeQuery = true)
    List<ComplaintIdTextDto> findAllByCommentId(long id);

    @Query(value = "select c.id, c.text, c.status from complaints c " +
            "where c.complainer_id = ?1 " +
            "order by c.create_date desc", nativeQuery = true)
    List<ComplaintIdTextDto> findAllByComplainerUserId(long id);
}
