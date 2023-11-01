package com.example.gym.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TraineeDTO {
    private UserDTO user;
    private LocalDate dateOfBirth;
    private String address;
}
