package com.project.recipe.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RatingReviewService {

    private final JdbcTemplate jdbcTemplate;

    public RatingReviewService(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Map<String, Object>> getRecipeRatings(
            String recipeId
    ) {

        String sql =
                """
                SELECT
                    rating_id,
                    user_id,
                    recipe_id,
                    rating,
                    created_at
                FROM ratings
                WHERE recipe_id = ?
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.queryForList(
                sql,
                recipeId
        );
    }


    public Double getAverageRating(
            String recipeId
    ) {

        String sql =
                """
                SELECT COALESCE(
                    ROUND(
                        AVG(rating)::numeric,
                        2
                    ),
                    0
                )
                FROM ratings
                WHERE recipe_id = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                Double.class,
                recipeId
        );
    }


    public List<Map<String, Object>> getRecipeReviews(
            String recipeId
    ) {

        String sql =
                """
                SELECT
                    r.review_id,
                    r.user_id,
                    u.full_name,
                    r.recipe_id,
                    r.review_text,
                    r.created_at
                FROM reviews r
                JOIN users u
                    ON r.user_id = u.user_id
                WHERE r.recipe_id = ?
                ORDER BY r.created_at DESC
                """;

        return jdbcTemplate.queryForList(
                sql,
                recipeId
        );
    }
}