package com.example.gym.repository;

import com.example.gym.model.User;
import com.example.gym.utility.DataPathsProperties;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Override
    default String getDataPath(DataPathsProperties paths) {
        return paths.getUser();
    }

    @Override
    default Class<User> getDataClass() {
        return User.class;
    }
}
