package com.example.gym.repository;

import com.example.gym.model.Trainee;
import com.example.gym.utility.DataPathsProperties;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends BaseRepository<Trainee> {
    List<Trainee> findByUserId(Long userId);
    @Override
    default String getDataPath(DataPathsProperties paths) {
        return paths.getTrainee();
    }

    @Override
    default Class<Trainee> getDataClass() {
        return Trainee.class;
    }
}
