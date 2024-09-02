package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.HashSet;
import java.util.Set;

@Data
public class NewCompilationDto {
    private Set<Long> events = new HashSet<>();

    private Boolean pinned = false;

    @Length(min = 1, max = 50)
    @NotBlank
    @NotEmpty
    private String title;
}
