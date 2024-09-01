package ru.practicum.explore.api.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.User;
import ru.practicum.explore.api.admin.service.AdminUserService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    public List<User> get(@RequestParam List<Long> ids) {
        return adminUserService.getAll(ids);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public User create(@Valid @RequestBody final User user) {
        return adminUserService.save(user);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        adminUserService.delete(userId);
    }
}
