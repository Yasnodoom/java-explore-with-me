package ru.practicum.dto.complaint;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.enums.ComplaintStatus;

import java.time.LocalDateTime;

import static ru.practicum.dto.enums.ComplaintStatus.NEW;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDto {
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 5, max = 2000)
    @Column(name = "text")
    private String text;

    @NotNull
    private Long commentId;

    private Long complainerId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ComplaintStatus status = NEW;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
}
