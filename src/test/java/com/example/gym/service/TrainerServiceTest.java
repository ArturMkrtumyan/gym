package com.example.gym.service;

import com.example.gym.dto.TrainerDTO;
import com.example.gym.dto.UserDTO;
import com.example.gym.exception.TrainerNotFoundException;
import com.example.gym.exception.TrainingTypeNotFoundException;
import com.example.gym.model.Trainer;
import com.example.gym.repository.TrainerRepository;
import com.example.gym.repository.TrainingTypeRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.utility.UserUtilService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserUtilService userUtilService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TrainerService trainerService;



    @Test
    void testCreateTrainer_TrainingTypeNotFoundException() {
        TrainerDTO trainerDTO = new TrainerDTO();
        trainerDTO.setUser(new UserDTO());
        trainerDTO.setSpecializationId(1L);

        when(trainingTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TrainingTypeNotFoundException.class, () -> trainerService.createTrainer(trainerDTO));
    }


    @Test
    void testUpdateTrainer_TrainerNotFoundException() {
        Long trainerId = 1L;
        TrainerDTO trainerDTO = new TrainerDTO();

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.updateTrainer(trainerId, trainerDTO));
    }

    @Test
    void testGetTrainerById() {
        Long trainerId = 1L;
        Trainer trainer = new Trainer();

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(trainer));

        TrainerDTO trainerDTO = trainerService.getTrainerById(trainerId);
        assertNull(trainerDTO);
    }

    @Test
    void testGetTrainerById_TrainerNotFoundException() {
        Long trainerId = 1L;

        when(trainerRepository.findById(trainerId)).thenReturn(Optional.empty());

        assertThrows(TrainerNotFoundException.class, () -> trainerService.getTrainerById(trainerId));
    }
}
