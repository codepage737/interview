package com.shahrabi.interview;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class DatabaseSanityCheckTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testPostgresConnectionAndVersion() {
        // Act: Query the standard PostgreSQL version function
        String pgVersion = jdbcTemplate.queryForObject("SELECT version();", String.class);

        // Assert: Verify the connection works and we are indeed running PostgreSQL 16
        System.out.println("Connected to: " + pgVersion);

        assertThat(pgVersion)
                .isNotNull()
                .contains("PostgreSQL 16");
    }
}
