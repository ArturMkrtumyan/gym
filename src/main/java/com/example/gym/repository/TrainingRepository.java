package com.example.gym.repository;

import com.example.gym.model.Training;
import com.example.gym.utility.DataPathsProperties;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends BaseRepository<Training> {
    List<Training> findByTraineeId(Long traineeId);
    List<Training> findByTrainerId(Long trainerId);
    List<Training> findByTrainingTypeId(Long trainingTypeId);

    @Override
    default String getDataPath(DataPathsProperties paths) {
        return paths.getTraining();
    }

    @Override
    default Class<Training> getDataClass() {
        return Training.class;
    }
}
