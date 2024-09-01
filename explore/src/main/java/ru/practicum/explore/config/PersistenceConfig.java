package ru.practicum.explore.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.practicum.explore")
@EntityScan("ru.practicum.dto")
public class PersistenceConfig {
}
