package ru.practicum.dto.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.User;
import ru.practicum.dto.user.UserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto
                .builder()
                .id(user.getUserId())
                .name(user.getName())
                .build();
    }
}
