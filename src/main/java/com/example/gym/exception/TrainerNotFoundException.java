package com.example.gym.exception;

public class TrainerNotFoundException extends RuntimeException{
    public TrainerNotFoundException(String message) {
        super(message);
    }
}
