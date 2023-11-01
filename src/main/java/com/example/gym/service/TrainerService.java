package com.example.gym.service;

import com.example.gym.dto.TrainerDTO;
import com.example.gym.exception.TrainerCreationException;
import com.example.gym.exception.TrainerNotFoundException;
import com.example.gym.exception.TrainingTypeNotFoundException;
import com.example.gym.exception.UserNotFoundException;
import com.example.gym.model.Trainer;
import com.example.gym.model.TrainingType;
import com.example.gym.model.User;
import com.example.gym.repository.TrainerRepository;
import com.example.gym.repository.TrainingTypeRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.utility.UserUtilService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper; // Import ModelMapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class TrainerService {
    private static final Logger logger = LogManager.getLogger(TrainerService.class);

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private UserUtilService userUtilService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public TrainerDTO createTrainer(TrainerDTO trainerDTO) {
        User user = userUtilService.createUserWithGeneratedUsernameAndPassword(
                trainerDTO.getUser().getFirstName(),
                trainerDTO.getUser().getLastName()
        );

        Trainer trainer = modelMapper.map(trainerDTO, Trainer.class);

        TrainingType specialization = trainingTypeRepository.findById(trainerDTO.getSpecializationId())
                .orElseThrow(() -> new TrainingTypeNotFoundException("Training type not found"));

        trainer.setUser(user);
        trainer.setSpecialization(specialization);

        try {
            userRepository.save(user);
            trainer = trainerRepository.save(trainer);
            logger.info("Trainer created with ID: {}", trainer.getId());
        } catch (DataIntegrityViolationException ex) {
            logger.error("Trainer creation failed. Check user data.", ex);
            throw new TrainerCreationException("Trainer creation failed. Check user data.");
        }

        return modelMapper.map(trainer, TrainerDTO.class);
    }

    @Transactional
    public TrainerDTO updateTrainer(Long trainerId, TrainerDTO trainerDTO) {
        Trainer existingTrainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found"));

        User user = existingTrainer.getUser();
        if (user == null) {
            logger.error("User not found for Trainer with ID: {}", trainerId);
            throw new UserNotFoundException("User not found for Trainer");
        }

        user.setFirstName(trainerDTO.getUser().getFirstName());
        user.setLastName(trainerDTO.getUser().getLastName());

        modelMapper.map(trainerDTO, existingTrainer);

        userRepository.save(user);
        return modelMapper.map(trainerRepository.save(existingTrainer), TrainerDTO.class);
    }

    public TrainerDTO getTrainerById(Long trainerId) {
        return modelMapper.map(trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found")), TrainerDTO.class);
    }
}
