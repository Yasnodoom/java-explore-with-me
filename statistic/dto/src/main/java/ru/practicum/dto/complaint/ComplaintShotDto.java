package ru.practicum.dto.complaint;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.enums.ComplaintStatus;

@Data
@Builder
public class ComplaintShotDto {
    private Long id;
    private String text;
    private ComplaintStatus status;
}
