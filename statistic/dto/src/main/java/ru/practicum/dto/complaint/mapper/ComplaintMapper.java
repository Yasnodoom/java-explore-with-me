package ru.practicum.dto.complaint.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.complaint.Complaint;
import ru.practicum.dto.complaint.ComplaintDto;
import ru.practicum.dto.complaint.ComplaintShotDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComplaintMapper {
    public static Complaint toComplaint(ComplaintDto dto) {
        return Complaint.builder()
                .text(dto.getText())
                .status(dto.getStatus())
                .create(dto.getCreated())
                .build();
    }

    public static ComplaintDto toComplaintDto(Complaint complaint) {
        return ComplaintDto.builder()
                .id(complaint.getId())
                .text(complaint.getText())
                .status(complaint.getStatus())
                .complainerId(complaint.getComplainer().getUserId())
                .commentId(complaint.getComment().getId())
                .build();
    }

    public static ComplaintShotDto toShotDto(Complaint complaint) {
        return ComplaintShotDto.builder()
                .id(complaint.getId())
                .text(complaint.getText())
                .status(complaint.getStatus())
                .build();
    }
}
