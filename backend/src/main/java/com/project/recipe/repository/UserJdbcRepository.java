package com.project.recipe.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Optional<Map<String, Object>> findByEmail(
            String email
    ) {

        String sql =
                """
                SELECT
                    user_id,
                    full_name,
                    email,
                    password_hash,
                    role,
                    is_active
                FROM users
                WHERE LOWER(email) = LOWER(?)
                """;

        List<Map<String, Object>> result =
                jdbcTemplate.queryForList(
                        sql,
                        email
                );

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                result.getFirst()
        );
    }


    public Optional<Map<String, Object>> findById(
            Long userId
    ) {

        String sql =
                """
                SELECT
                    user_id,
                    full_name,
                    email,
                    role,
                    is_active
                FROM users
                WHERE user_id = ?
                """;

        List<Map<String, Object>> result =
                jdbcTemplate.queryForList(
                        sql,
                        userId
                );

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                result.getFirst()
        );
    }


    public Long createUser(

            String fullName,

            String email,

            String passwordHash

    ) {

        String sql =
                """
                INSERT INTO users (
                    full_name,
                    email,
                    password_hash,
                    role,
                    is_active
                )
                VALUES (
                    ?,
                    ?,
                    ?,
                    'USER',
                    true
                )
                RETURNING user_id
                """;

        return jdbcTemplate.queryForObject(

                sql,

                Long.class,

                fullName,
                email,
                passwordHash
        );
    }
}