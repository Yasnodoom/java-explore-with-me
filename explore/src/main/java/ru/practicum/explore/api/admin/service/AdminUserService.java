package ru.practicum.explore.api.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.User;
import ru.practicum.explore.exception.NotFoundException;
import ru.practicum.explore.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;

    public List<User> getAll(List<Long> ids) {
        return userRepository.findAllById(ids);
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
