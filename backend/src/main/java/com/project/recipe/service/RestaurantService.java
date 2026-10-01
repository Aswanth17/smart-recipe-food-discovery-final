package com.project.recipe.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantService {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantService(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Map<String, Object>> getAllRestaurants() {

        String sql =
                """
                SELECT
                    restaurant_id,
                    name,
                    address,
                    rating,
                    ST_Y(location::geometry)
                        AS latitude,
                    ST_X(location::geometry)
                        AS longitude
                FROM restaurants
                ORDER BY name
                """;

        return jdbcTemplate.queryForList(sql);
    }


    public Map<String, Object> getRestaurantById(
            Long restaurantId
    ) {

        String restaurantSql =
                """
                SELECT
                    restaurant_id,
                    name,
                    address,
                    rating,
                    ST_Y(location::geometry)
                        AS latitude,
                    ST_X(location::geometry)
                        AS longitude
                FROM restaurants
                WHERE restaurant_id = ?
                """;


        List<Map<String, Object>> restaurants =
                jdbcTemplate.queryForList(

                        restaurantSql,

                        restaurantId
                );


        if (restaurants.isEmpty()) {

            throw new IllegalArgumentException(
                    "Restaurant not found: "
                            + restaurantId
            );
        }


        String dishesSql =
                """
                SELECT
                    dish_id,
                    restaurant_id,
                    recipe_id,
                    dish_name,
                    price
                FROM restaurant_dishes
                WHERE restaurant_id = ?
                ORDER BY dish_name
                """;


        List<Map<String, Object>> dishes =
                jdbcTemplate.queryForList(

                        dishesSql,

                        restaurantId
                );


        Map<String, Object> response =
                new LinkedHashMap<>(

                        restaurants.getFirst()
                );


        response.put(
                "dishes",
                dishes
        );


        return response;
    }
}