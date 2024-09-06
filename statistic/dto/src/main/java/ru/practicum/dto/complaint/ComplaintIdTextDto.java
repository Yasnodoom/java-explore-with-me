package ru.practicum.dto.complaint;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public interface ComplaintIdTextDto {
    Long getId();

    String getText();

    @Enumerated(EnumType.STRING)
    String getStatus();
}
