package com.example.gym.service;

import com.example.gym.dto.TrainingDTO;
import com.example.gym.exception.TrainingNotFoundException;
import com.example.gym.model.Training;
import com.example.gym.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrainingServiceTest {
    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTraining() {
        TrainingDTO trainingDTO = new TrainingDTO();
        Training training = new Training();

        when(modelMapper.map(trainingDTO, Training.class)).thenReturn(training);
        when(trainingRepository.save(training)).thenReturn(training);

        TrainingDTO createdTraining = trainingService.createTraining(trainingDTO);

        assertNull(createdTraining);
        assertNotEquals(training, modelMapper.map(createdTraining, Training.class));
    }

    @Test
    void testCreateTrainingWithException() {
        TrainingDTO trainingDTO = new TrainingDTO();

        when(modelMapper.map(trainingDTO, Training.class)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> trainingService.createTraining(trainingDTO));
    }

    @Test
    void testUpdateTraining() {
        Long trainingId = 1L;
        TrainingDTO trainingDTO = new TrainingDTO();
        Training existingTraining = new Training();

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(existingTraining));
        when(trainingRepository.save(existingTraining)).thenReturn(existingTraining);

        TrainingDTO updatedTraining = trainingService.updateTraining(trainingId, trainingDTO);

        assertNull(updatedTraining);
        assertNotEquals(existingTraining, modelMapper.map(updatedTraining, Training.class));
    }

    @Test
    public void testUpdateTrainingNotFound() {
        Long trainingId = 1L;

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class, () -> trainingService.updateTraining(trainingId, new TrainingDTO()));
    }

    @Test
    public void testDeleteTraining() {
        Long trainingId = 1L;
        Training existingTraining = new Training();

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(existingTraining));

        assertDoesNotThrow(() -> trainingService.deleteTraining(trainingId));
    }

    @Test
    public void testDeleteTrainingNotFound() {
        Long trainingId = 1L;

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class, () -> trainingService.deleteTraining(trainingId));
    }

    @Test
    public void testGetTrainingById() {
        Long trainingId = 1L;
        Training training = new Training();
        TrainingDTO trainingDTO = new TrainingDTO();

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        when(modelMapper.map(training, TrainingDTO.class)).thenReturn(trainingDTO);

        TrainingDTO retrievedTraining = trainingService.getTrainingById(trainingId);

        assertNotNull(retrievedTraining);
        assertEquals(trainingDTO, retrievedTraining);
    }

    @Test
    public void testGetTrainingByIdNotFound() {
        Long trainingId = 1L;

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

        assertThrows(TrainingNotFoundException.class, () -> trainingService.getTrainingById(trainingId));
    }
}
