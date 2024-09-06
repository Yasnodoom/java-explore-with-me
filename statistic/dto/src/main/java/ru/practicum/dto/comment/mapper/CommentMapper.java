package ru.practicum.dto.comment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.comment.Comment;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.event.mapper.EventMapper;
import ru.practicum.dto.user.mapper.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentFullDto toFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .event(EventMapper.toEventShotDto(comment.getEvent()))
                .created(comment.getCreate())
                .build();
    }

    public static Comment toComment(NewCommentDto dto) {
        return Comment.builder()
                .text(dto.getText())
                .create(dto.getCreated())
                .build();
    }
}


