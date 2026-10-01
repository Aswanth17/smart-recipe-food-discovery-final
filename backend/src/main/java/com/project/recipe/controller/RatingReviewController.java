package com.project.recipe.controller;

import com.project.recipe.service.RatingReviewService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RatingReviewController {

    private final RatingReviewService ratingReviewService;

    public RatingReviewController(
            RatingReviewService ratingReviewService
    ) {
        this.ratingReviewService =
                ratingReviewService;
    }


    @GetMapping("/{id}/ratings")
    public List<Map<String, Object>> ratings(
            @PathVariable String id
    ) {

        return ratingReviewService
                .getRecipeRatings(id);
    }


    @GetMapping("/{id}/average-rating")
    public Map<String, Object> averageRating(
            @PathVariable String id
    ) {

        return Map.of(
                "recipeId",
                id,

                "averageRating",
                ratingReviewService
                        .getAverageRating(id)
        );
    }


    @GetMapping("/{id}/reviews")
    public List<Map<String, Object>> reviews(
            @PathVariable String id
    ) {

        return ratingReviewService
                .getRecipeReviews(id);
    }
}