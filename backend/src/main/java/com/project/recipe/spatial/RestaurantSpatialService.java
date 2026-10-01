package com.project.recipe.spatial;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RestaurantSpatialService {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantSpatialService(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Map<String, Object>> findNearby(

            double latitude,
            double longitude,
            int radiusMeters

    ) {

        String sql =
                """
                SELECT *
                FROM find_nearby_restaurants(
                    CAST(? AS DOUBLE PRECISION),
                    CAST(? AS DOUBLE PRECISION),
                    CAST(? AS INTEGER)
                )
                """;

        return jdbcTemplate.queryForList(

                sql,

                latitude,
                longitude,
                radiusMeters
        );
    }


    public List<Map<String, Object>> findRestaurantsServingRecipe(

            double latitude,
            double longitude,
            int radiusMeters,
            String recipeId

    ) {

        String sql =
                """
                SELECT *
                FROM find_restaurants_serving_recipe(
                    CAST(? AS DOUBLE PRECISION),
                    CAST(? AS DOUBLE PRECISION),
                    CAST(? AS INTEGER),
                    CAST(? AS VARCHAR)
                )
                """;

        return jdbcTemplate.queryForList(

                sql,

                latitude,
                longitude,
                radiusMeters,
                recipeId
        );
    }


    public Map<String, Object> findNearest(

            double latitude,
            double longitude

    ) {

        String sql =
                """
                SELECT *
                FROM find_nearest_restaurant(
                    CAST(? AS DOUBLE PRECISION),
                    CAST(? AS DOUBLE PRECISION)
                )
                """;

        List<Map<String, Object>> result =
                jdbcTemplate.queryForList(

                        sql,

                        latitude,
                        longitude
                );

        if (result.isEmpty()) {

            throw new IllegalStateException(
                    "No restaurant found."
            );
        }

        return result.getFirst();
    }


    public Map<String, Object> findNearestForRecipe(

            double latitude,
            double longitude,
            String recipeId

    ) {

        String sql =
                """
                SELECT *
                FROM find_nearest_restaurant_for_recipe(
                    CAST(? AS DOUBLE PRECISION),
                    CAST(? AS DOUBLE PRECISION),
                    CAST(? AS VARCHAR)
                )
                """;

        List<Map<String, Object>> result =
                jdbcTemplate.queryForList(

                        sql,

                        latitude,
                        longitude,
                        recipeId
                );

        if (result.isEmpty()) {

            throw new IllegalStateException(
                    "No restaurant found for recipe: "
                            + recipeId
            );
        }

        return result.getFirst();
    }
}