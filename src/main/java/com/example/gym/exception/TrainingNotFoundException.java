package com.example.gym.exception;

public class TrainingNotFoundException extends RuntimeException{
    public TrainingNotFoundException(String message) {
        super(message);
    }
}
