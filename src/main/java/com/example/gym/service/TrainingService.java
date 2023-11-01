package com.example.gym.service;

import com.example.gym.dto.TrainingDTO;
import com.example.gym.exception.TrainingCreationException;
import com.example.gym.exception.TrainingNotFoundException;
import com.example.gym.model.Training;
import com.example.gym.repository.TrainingRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private static final Logger logger = LogManager.getLogger(TrainingService.class);

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public TrainingDTO createTraining(TrainingDTO trainingDTO) {
        Training training = modelMapper.map(trainingDTO, Training.class);

        try {
            training = trainingRepository.save(training);
            logger.info("Training created with ID: {}", training.getId());
        } catch (Exception ex) {
            logger.error("Training creation failed.", ex);
            throw new TrainingCreationException("Training creation failed.");
        }

        return modelMapper.map(training, TrainingDTO.class);
    }

    @Transactional
    public TrainingDTO updateTraining(Long trainingId, TrainingDTO trainingDTO) {
        Training existingTraining = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found"));

        modelMapper.map(trainingDTO, existingTraining);

        return modelMapper.map(trainingRepository.save(existingTraining), TrainingDTO.class);
    }

    @Transactional
    public void deleteTraining(Long trainingId) {
        Training existingTraining = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found"));

        trainingRepository.delete(existingTraining);
    }

    public TrainingDTO getTrainingById(Long trainingId) {
        return modelMapper.map(trainingRepository.findById(trainingId)
                .orElseThrow(() -> new TrainingNotFoundException("Training not found")), TrainingDTO.class);
    }
}
