package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.complaint.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}
