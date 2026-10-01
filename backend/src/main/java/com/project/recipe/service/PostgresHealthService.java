package com.project.recipe.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostgresHealthService {

    private final JdbcTemplate jdbcTemplate;

    public PostgresHealthService(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getDatabaseVersion() {

        return jdbcTemplate.queryForObject(
                "SELECT version()",
                String.class
        );
    }

    public int getUserCount() {

        Integer count =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM users",
                        Integer.class
                );

        return count == null ? 0 : count;
    }

    public int getRestaurantCount() {

        Integer count =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM restaurants",
                        Integer.class
                );

        return count == null ? 0 : count;
    }

    public String getPostgisVersion() {

        return jdbcTemplate.queryForObject(
                "SELECT PostGIS_Version()",
                String.class
        );
    }
}