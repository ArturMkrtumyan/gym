package com.example.gym.utility;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("data.path")
public class DataPathsProperties {

    private String trainee;
    private String trainer;
    private String training;
    private String trainingType;
    private String user;

}
