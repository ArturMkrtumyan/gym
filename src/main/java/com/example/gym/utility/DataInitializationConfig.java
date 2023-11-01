package com.example.gym.utility;

import com.example.gym.model.*;
import com.example.gym.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {

    private final DataPathsProperties dataPathsProperties;
    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @PostConstruct
    public void initializeData() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        initializeRepository(userRepository, dataPathsProperties.getUser(), objectMapper, User.class);
        initializeRepository(traineeRepository, dataPathsProperties.getTrainee(), objectMapper, Trainee.class);
        initializeRepository(trainerRepository, dataPathsProperties.getTrainer(), objectMapper, Trainer.class);
        initializeRepository(trainingRepository, dataPathsProperties.getTraining(), objectMapper, Training.class);
        initializeRepository(trainingTypeRepository, dataPathsProperties.getTrainingType(), objectMapper, TrainingType.class);
    }

    private <T> void initializeRepository(
            JpaRepository<T, Long> repository, String dataPath, ObjectMapper objectMapper, Class<T> entityClass
    ) {
        try {
            List<T> data = objectMapper.readValue(
                    resourceLoader.getResource(dataPath).getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, entityClass)
            );
            repository.saveAll(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data from files.", e);
        }
    }
}
