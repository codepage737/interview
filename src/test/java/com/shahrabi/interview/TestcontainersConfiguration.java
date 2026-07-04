package com.shahrabi.interview;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.application.name}")
    private String databaseName;

    @Bean
    @ServiceConnection(name = "postgresql")
    PostgreSQLContainer<?> postgresql() {
        DockerImageName image = DockerImageName.parse("docker.arvancloud.ir/postgres:16-alpine:latest")
                .asCompatibleSubstituteFor("postgres");

        return new PostgreSQLContainer<>(image)
                .withTmpFs(Map.of("/var/lib/postgresql/data", "rw"))
                .withUsername(username)
                .withPassword(password)
                .withDatabaseName(databaseName)
                .withReuse(true);
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry, PostgreSQLContainer<?> postgresql) {
        registry.add("spring.datasource.url", postgresql::getJdbcUrl);
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("spring.datasource.password", postgresql::getPassword);
    }
}
