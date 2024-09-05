package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
