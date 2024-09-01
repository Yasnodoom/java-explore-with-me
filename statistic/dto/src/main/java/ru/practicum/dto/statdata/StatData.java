package ru.practicum.dto.statdata;

import lombok.Data;

@Data
public class StatData {
    private String app;
    private String uri;
    private Integer hits;
}
