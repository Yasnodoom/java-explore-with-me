package ru.practicum.explore.api.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.User;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;

    public List<User> getAll(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return userRepository.findAllUsers(PageRequest.of(from, size));
        }
        return userRepository.findAllByUserIdIn(ids, PageRequest.of(from, size));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }
}
