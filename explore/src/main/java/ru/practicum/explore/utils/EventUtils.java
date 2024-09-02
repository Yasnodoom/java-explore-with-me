package ru.practicum.explore.utils;

import ru.practicum.dto.enums.UserStateAction;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.StateActionEvent;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.ValidationException;

import java.time.LocalDateTime;

import static ru.practicum.dto.event.Status.*;

public class EventUtils {
    public static void updateStatusByAdmin(Event event, StateActionEvent stateAction) {
        if (stateAction == null) {
            return;
        }
        switch (stateAction) {
            case PUBLISH_EVENT -> {
                if (event.getState().equals(PUBLISHED) || event.getState().equals(CANCELED) ) {
                    throw new ConflictException("already published");
                }
                event.setPublishedOn(LocalDateTime.now());
                event.setState(PUBLISHED);
            }
            case REJECT_EVENT -> {
                if (event.getState().equals(PUBLISHED)) {
                    throw new ConflictException("mes1");
                }
                event.setState(CANCELED);
            }
            default -> throw new IllegalStateException("Unexpected value: " + stateAction);
        }
    }

    public static void updateStatusByUser(Event event, UserStateAction action) {
        if (action ==  null) {
            return;
        }
        switch (action) {
            case SEND_TO_REVIEW -> {
                if (event.getState().equals(CANCELED)) {
                    event.setState(PENDING);
                }
                if (event.getState().equals(PUBLISHED)) {
                    throw new ValidationException("is published");
                }
            }
            case CANCEL_REVIEW -> {
                if (event.getState().equals(PUBLISHED)) {
                    throw new ValidationException("is published");
                }
                event.setState(CANCELED);
            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }
}
