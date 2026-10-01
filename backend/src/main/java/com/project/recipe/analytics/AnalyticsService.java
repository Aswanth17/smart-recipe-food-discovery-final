package com.project.recipe.analytics;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final JdbcTemplate jdbcTemplate;

    public AnalyticsService(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Map<String, Object>> topRatedRecipes() {

        String sql =
                """
                SELECT
                    rm.recipe_id,
                    ROUND(
                        AVG(r.rating)::numeric,
                        2
                    ) AS average_rating,
                    COUNT(r.rating_id) AS rating_count
                FROM recipe_metadata rm
                JOIN ratings r
                    ON rm.recipe_id = r.recipe_id
                GROUP BY rm.recipe_id
                ORDER BY
                    average_rating DESC,
                    rating_count DESC
                LIMIT 10
                """;

        return jdbcTemplate.queryForList(sql);
    }


   public List<Map<String, Object>> cuisinePopularity() {

    String sql =
            """
            SELECT
                c.name AS cuisine,
                COUNT(*) AS interaction_count
            FROM activity_events a
            JOIN recipe_metadata rm
                ON a.recipe_id = rm.recipe_id
            JOIN cuisines c
                ON rm.cuisine_id = c.cuisine_id
            GROUP BY c.name
            ORDER BY interaction_count DESC
            """;

    return jdbcTemplate.queryForList(sql);
}


    public List<Map<String, Object>> mostViewedRecipes() {

        String sql =
                """
                SELECT
                    recipe_id,
                    COUNT(*) AS view_count
                FROM activity_events
                WHERE event_type = 'VIEW_RECIPE'
                GROUP BY recipe_id
                ORDER BY view_count DESC
                LIMIT 10
                """;

        return jdbcTemplate.queryForList(sql);
    }


    public List<Map<String, Object>> restaurantDishCounts() {

    String sql =
            """
            SELECT
                r.restaurant_id,
                r.name,
                COUNT(*) AS dish_count
            FROM restaurants r
            JOIN restaurant_dishes rd
                ON r.restaurant_id = rd.restaurant_id
            GROUP BY
                r.restaurant_id,
                r.name
            ORDER BY dish_count DESC
            """;

    return jdbcTemplate.queryForList(sql);
}


    public Map<String, Object> dashboardSummary() {

        Integer users =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM users",
                        Integer.class
                );

        Integer recipes =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM recipe_metadata",
                        Integer.class
                );

        Integer restaurants =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM restaurants",
                        Integer.class
                );

        Integer ratings =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM ratings",
                        Integer.class
                );

        Integer reviews =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM reviews",
                        Integer.class
                );

        Integer events =
                jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM activity_events",
                        Integer.class
                );


        return Map.of(
                "users", users,
                "recipes", recipes,
                "restaurants", restaurants,
                "ratings", ratings,
                "reviews", reviews,
                "activityEvents", events
        );
    }
}