package com.example.gym.utility;

import com.example.gym.repository.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {

    private final DataPathsProperties dataPathsProperties;
    private final ResourceLoader resourceLoader;
    private final List<BaseRepository<?>> repositories;

    @PostConstruct
    public void initializeData() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        repositories.forEach(repository ->
                repository.loadData(resourceLoader, objectMapper, dataPathsProperties)
        );
    }
}
