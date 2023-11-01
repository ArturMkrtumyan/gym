package com.example.gym.service;

import com.example.gym.dto.TraineeDTO;
import com.example.gym.exception.TraineeCreationException;
import com.example.gym.exception.TraineeNotFoundException;
import com.example.gym.exception.UserNotFoundException;
import com.example.gym.model.Trainee;
import com.example.gym.model.User;
import com.example.gym.repository.TraineeRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.utility.UserUtilService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Service
public class TraineeService {

    private static final Logger logger = LogManager.getLogger(TraineeService.class);

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private UserUtilService userUtilService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public TraineeDTO createTrainee(TraineeDTO traineeDTO) {
        User user = userUtilService.createUserWithGeneratedUsernameAndPassword(
                traineeDTO.getUser().getFirstName(),
                traineeDTO.getUser().getLastName()
        );

        Trainee trainee = modelMapper.map(traineeDTO, Trainee.class);

        trainee.setUser(user);

        try {
            userRepository.save(user);
            trainee = traineeRepository.save(trainee);
            logger.info("Trainee created with ID: {}", trainee.getId());
        } catch (DataIntegrityViolationException ex) {
            logger.error("Trainee creation failed. Check user data.", ex);
            throw new TraineeCreationException("Trainee creation failed. Check user data.");
        }

        return modelMapper.map(trainee, TraineeDTO.class);
    }

    @Transactional
    public TraineeDTO updateTrainee(Long traineeId, TraineeDTO traineeDTO) {
        Trainee existingTrainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found"));

        User user = existingTrainee.getUser();
        if (user == null) {
            logger.error("User not found for Trainee with ID: {}", traineeId);
            throw new UserNotFoundException("User not found for Trainee");
        }

        user.setFirstName(traineeDTO.getUser().getFirstName());
        user.setLastName(traineeDTO.getUser().getLastName());

        modelMapper.map(traineeDTO, existingTrainee);

        userRepository.save(user);
        return modelMapper.map(traineeRepository.save(existingTrainee), TraineeDTO.class);
    }

    @Transactional
    public void deleteTrainee(Long traineeId) {
        Trainee existingTrainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found"));

        traineeRepository.delete(existingTrainee);
    }

    public TraineeDTO getTraineeById(Long traineeId) {
        return modelMapper.map(traineeRepository.findById(traineeId)
                .orElseThrow(() -> new TraineeNotFoundException("Trainee not found")), TraineeDTO.class);
    }

    public List<TraineeDTO> getAllTrainees() {
        List<Trainee> trainees = traineeRepository.findAll();
        return trainees.stream()
                .map(trainee -> modelMapper.map(trainee, TraineeDTO.class))
                .toList();
    }
}
