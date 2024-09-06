package ru.practicum.explore.api.closed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.Comment;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.mapper.CommentMapper;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment getCommentById(long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public void deleteCommentById(long id) {
        commentRepository.deleteById(id);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<CommentFullDto> getAllCommentsByEventId(long eventId) {
        return commentRepository
                .findAllByEventId(eventId)
                .stream()
                .map(CommentMapper::toFullDto)
                .toList();
    }
}
