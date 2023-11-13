package com.example.spring_dblab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EventEditDto {
    private long eventCode;
    private String name;
    private String description;
    private int maxParticipation;
}
