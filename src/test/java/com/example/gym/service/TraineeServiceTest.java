package com.example.gym.service;

import com.example.gym.dto.TraineeDTO;
import com.example.gym.dto.UserDTO;
import com.example.gym.exception.TraineeNotFoundException;
import com.example.gym.exception.UserNotFoundException;
import com.example.gym.model.Trainee;
import com.example.gym.model.User;
import com.example.gym.repository.TraineeRepository;
import com.example.gym.repository.UserRepository;
import com.example.gym.utility.UserUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserUtilService userUtilService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTrainee() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setUser(new UserDTO());

        User user = new User();
        when(userUtilService.createUserWithGeneratedUsernameAndPassword(Mockito.any(), Mockito.any())).thenReturn(user);

        Trainee trainee = new Trainee();
        when(modelMapper.map(traineeDTO, Trainee.class)).thenReturn(trainee);

        when(userRepository.save(user)).thenReturn(user);
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        TraineeDTO createdTrainee = traineeService.createTrainee(traineeDTO);
    }

    @Test
    void testCreateTraineeException() {
        TraineeDTO traineeDTO = new TraineeDTO();
        when(userUtilService.createUserWithGeneratedUsernameAndPassword(Mockito.any(), Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(NullPointerException.class, () -> traineeService.createTrainee(traineeDTO));
    }

    @Test
    void testUpdateTrainee() {
        Long traineeId = 1L;
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setUser(new UserDTO());

        // Create an existing Trainee with a valid User
        Trainee existingTrainee = new Trainee();
        User existingUser = new User();
        existingTrainee.setUser(existingUser);

        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(existingTrainee));
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        when(traineeRepository.save(existingTrainee)).thenReturn(existingTrainee);

        TraineeDTO updatedTrainee = traineeService.updateTrainee(traineeId, traineeDTO);
    }


    @Test
    void testUpdateTraineeNotFound() {
        Long traineeId = 1L;
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.updateTrainee(traineeId, new TraineeDTO()));
    }

    @Test
    void testUpdateTraineeUserNotFound() {
        Long traineeId = 1L;
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setUser(new UserDTO());

        Trainee existingTrainee = new Trainee();
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(existingTrainee));

        assertThrows(UserNotFoundException.class, () -> traineeService.updateTrainee(traineeId, traineeDTO));
    }

    @Test
    void testDeleteTrainee() {
        Long traineeId = 1L;
        Trainee existingTrainee = new Trainee();
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(existingTrainee));

        traineeService.deleteTrainee(traineeId);

        Mockito.verify(traineeRepository).delete(existingTrainee);
    }

    @Test
    void testDeleteTraineeNotFound() {
        Long traineeId = 1L;
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.deleteTrainee(traineeId));
    }

    @Test
    void testGetTraineeById() {
        Long traineeId = 1L;
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setUser(new UserDTO());

        Trainee trainee = new Trainee();
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));

        when(modelMapper.map(trainee, TraineeDTO.class)).thenReturn(traineeDTO);

        TraineeDTO retrievedTrainee = traineeService.getTraineeById(traineeId);
        assertNotNull(retrievedTrainee);
    }

    @Test
    void testGetTraineeByIdNotFound() {
        Long traineeId = 1L;
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.empty());

        assertThrows(TraineeNotFoundException.class, () -> traineeService.getTraineeById(traineeId));
    }

    @Test
    void testGetAllTrainees() {
        List<Trainee> trainees = List.of(new Trainee(), new Trainee());
        when(traineeRepository.findAll()).thenReturn(trainees);

        List<TraineeDTO> traineeDTOs = traineeService.getAllTrainees();
        assertEquals(trainees.size(), traineeDTOs.size());
    }
}
