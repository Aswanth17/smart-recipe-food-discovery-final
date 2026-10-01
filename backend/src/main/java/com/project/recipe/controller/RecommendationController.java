package com.project.recipe.controller;

import com.project.recipe.model.RecommendationResult;

import com.project.recipe.service.RecommendationOrchestrationService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationOrchestrationService recommendationService;


    public RecommendationController(
            RecommendationOrchestrationService recommendationService
    ) {

        this.recommendationService =
                recommendationService;
    }


    @GetMapping("/{userId}")
    public List<RecommendationResult> getRecommendations(

            @PathVariable Long userId,

            @RequestParam(
                    required = false,
                    defaultValue = ""
            )
            String ingredients

    ) {

        List<String> availableIngredients =
                ingredients.isBlank()
                        ? List.of()
                        : Arrays.stream(
                                ingredients.split(",")
                        )
                        .map(String::trim)
                        .filter(
                                value ->
                                        !value.isBlank()
                        )
                        .toList();


        return recommendationService
                .recommend(
                        userId,
                        availableIngredients
                );
    }
}