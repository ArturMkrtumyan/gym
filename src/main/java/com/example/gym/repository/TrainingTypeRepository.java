package com.example.gym.repository;

import com.example.gym.model.TrainingType;
import com.example.gym.utility.DataPathsProperties;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainingTypeRepository extends BaseRepository<TrainingType> {

    @Override
    default String getDataPath(DataPathsProperties paths) {
        return paths.getTrainingType();
    }

    @Override
    default Class<TrainingType> getDataClass() {
        return TrainingType.class;
    }
}
