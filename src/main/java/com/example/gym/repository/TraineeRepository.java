package com.example.gym.repository;

import com.example.gym.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    List<Trainee> findByUserId(Long userId);
}
