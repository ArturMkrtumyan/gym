package com.example.gym.repository;

import com.example.gym.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByTraineeId(Long traineeId);
    List<Training> findByTrainerId(Long trainerId);
    List<Training> findByTrainingTypeId(Long trainingTypeId);
}
