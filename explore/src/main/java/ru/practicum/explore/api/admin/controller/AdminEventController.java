package ru.practicum.explore.api.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.complaint.Complaint;
import ru.practicum.dto.enums.ComplaintStatus;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore.api.admin.service.AdminEventService;
import ru.practicum.explore.api.closed.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminEventController {
    private final AdminEventService service;
    private final CommentService commentService;

    @Transactional
    @GetMapping("/events")
    public List<EventFullDto> findAll(HttpServletRequest request,
                                      @RequestParam(required = false) List<Long> users,
                                      @RequestParam(required = false) List<String> states,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return service.findAll(request, users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patch(@PathVariable long eventId,
                              @RequestBody final UpdateEventAdminRequest data) {
        return service.updateEvent(eventId, data);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteComment(@PathVariable(name = "commentId") long commentId) {
        commentService.deleteCommentById(commentId);
    }

    @PatchMapping("/complaints/{complaintId}")
    public Complaint considerComplaint(
            @PathVariable(name = "complaintId") long complaintId,
            @RequestParam ComplaintStatus status) {
        return service.considerComplaint(complaintId, status);
    }
}