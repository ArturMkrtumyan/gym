package com.example.gym.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TrainingDTO {
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private Long trainingTypeId;
    private LocalDate trainingDate;
    private Integer trainingDuration;
}
