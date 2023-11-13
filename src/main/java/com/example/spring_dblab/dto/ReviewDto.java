package com.example.spring_dblab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReviewDto {
    private long eventId;
    private String review;
    private int score;
}
