package com.example.gym.repository;

import com.example.gym.model.Trainer;
import com.example.gym.utility.DataPathsProperties;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends BaseRepository<Trainer> {
    List<Trainer> findBySpecializationId(Long specializationId);
    List<Trainer> findByUserId(Long userId);

    @Override
    default String getDataPath(DataPathsProperties paths) {
        return paths.getTrainer();
    }

    @Override
    default Class<Trainer> getDataClass() {
        return Trainer.class;
    }
}
