package com.project.recipe.controller;

import com.project.recipe.service.RestaurantService;
import com.project.recipe.spatial.RestaurantSpatialService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    private final RestaurantSpatialService spatialService;


    public RestaurantController(

            RestaurantService restaurantService,

            RestaurantSpatialService spatialService

    ) {

        this.restaurantService =
                restaurantService;

        this.spatialService =
                spatialService;
    }


    @GetMapping
    public List<Map<String, Object>> getAllRestaurants() {

        return restaurantService
                .getAllRestaurants();
    }


    @GetMapping("/{id}")
    public Map<String, Object> getRestaurant(

            @PathVariable Long id

    ) {

        return restaurantService
                .getRestaurantById(id);
    }


    @GetMapping("/nearby")
    public List<Map<String, Object>> nearby(

            @RequestParam double lat,

            @RequestParam double lon,

            @RequestParam(
                    defaultValue = "5000"
            )
            int radius

    ) {

        return spatialService
                .findNearby(
                        lat,
                        lon,
                        radius
                );
    }


    @GetMapping("/nearest")
    public Map<String, Object> nearest(

            @RequestParam double lat,

            @RequestParam double lon

    ) {

        return spatialService
                .findNearest(
                        lat,
                        lon
                );
    }


    @GetMapping("/serving/{recipeId}")
    public List<Map<String, Object>> servingRecipe(

            @PathVariable String recipeId,

            @RequestParam double lat,

            @RequestParam double lon,

            @RequestParam(
                    defaultValue = "5000"
            )
            int radius

    ) {

        return spatialService
                .findRestaurantsServingRecipe(

                        lat,
                        lon,
                        radius,
                        recipeId
                );
    }


    @GetMapping("/nearest/{recipeId}")
    public Map<String, Object> nearestForRecipe(

            @PathVariable String recipeId,

            @RequestParam double lat,

            @RequestParam double lon

    ) {

        return spatialService
                .findNearestForRecipe(

                        lat,
                        lon,
                        recipeId
                );
    }
}