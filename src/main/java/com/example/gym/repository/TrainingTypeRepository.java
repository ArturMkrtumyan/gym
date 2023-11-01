package com.example.gym.repository;

import com.example.gym.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
}
