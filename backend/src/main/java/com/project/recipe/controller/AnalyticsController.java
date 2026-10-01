package com.project.recipe.controller;

import com.project.recipe.analytics.AnalyticsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(
            AnalyticsService analyticsService
    ) {
        this.analyticsService =
                analyticsService;
    }


    @GetMapping("/summary")
    public Map<String, Object> summary() {

        return analyticsService
                .dashboardSummary();
    }


    @GetMapping("/top-rated-recipes")
    public List<Map<String, Object>> topRatedRecipes() {

        return analyticsService
                .topRatedRecipes();
    }


    @GetMapping("/cuisine-popularity")
    public List<Map<String, Object>> cuisinePopularity() {

        return analyticsService
                .cuisinePopularity();
    }


    @GetMapping("/most-viewed-recipes")
    public List<Map<String, Object>> mostViewedRecipes() {

        return analyticsService
                .mostViewedRecipes();
    }


    @GetMapping("/restaurant-dish-counts")
    public List<Map<String, Object>> restaurantDishCounts() {

        return analyticsService
                .restaurantDishCounts();
    }
}