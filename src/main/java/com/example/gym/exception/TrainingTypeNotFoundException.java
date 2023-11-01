package com.example.gym.exception;

public class TrainingTypeNotFoundException extends RuntimeException{
    public TrainingTypeNotFoundException(String message) {
        super(message);
    }
}
