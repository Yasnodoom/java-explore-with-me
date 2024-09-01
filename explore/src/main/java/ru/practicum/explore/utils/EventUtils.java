package ru.practicum.explore.utils;

import ru.practicum.dto.enums.UserStateAction;
import ru.practicum.dto.event.Event;
import ru.practicum.dto.event.StateActionEvent;
import ru.practicum.explore.exception.ValidationException;

import java.time.LocalDateTime;

import static ru.practicum.dto.event.Status.*;

public class EventUtils {
    public static void updateStatusByAdmin(Event event, StateActionEvent stateAction) {
        switch (stateAction) {
            case PUBLISH_EVENT -> {
//                if (!event.getState().equals(WAITING)) {
//                    throw new ValidationException("mes1");
//                }
                event.setPublished(LocalDateTime.now());
                event.setState(PUBLISHED);
            }
            case REJECT_EVENT -> {
                if (event.getState().equals(PUBLISHED)) {
                    throw new ValidationException("mes1");
                }
                event.setState(CANCELED);
            }
            default -> throw new IllegalStateException("Unexpected value: " + stateAction);
        }
    }

    public static void updateStatusByUser(Event event, UserStateAction action) {
        switch (action) {
            case SEND_TO_REVIEW -> {
                if (!event.getState().equals(WAITING)) {
                    throw new ValidationException("now in waiting");
                }
                event.setPublished(LocalDateTime.now());
                event.setState(WAITING);
            }
            case CANCEL_REVIEW -> {
                if (event.getState().equals(PUBLISHED)) {
                    throw new ValidationException("mes1");
                }
                event.setState(CANCELED);
            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
    }
}
