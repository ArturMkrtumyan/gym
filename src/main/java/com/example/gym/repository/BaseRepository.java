package com.example.gym.repository;

import com.example.gym.utility.DataPathsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long> {

    Class<T> getDataClass();

    String getDataPath(DataPathsProperties paths);

    default void loadData(ResourceLoader resourceLoader, ObjectMapper objectMapper, DataPathsProperties dataPaths){
        try {
            var dataPath = getDataPath(dataPaths);
            var data = objectMapper.<List<T>>readValue(
                    resourceLoader.getResource(dataPath).getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, getDataClass())
            );
            saveAll(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data from files.", e);
        }
    }
}
