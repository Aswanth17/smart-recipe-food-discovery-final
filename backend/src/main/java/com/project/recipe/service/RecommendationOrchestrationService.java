package com.project.recipe.service;

import com.project.recipe.config.RecommendationWeights;

import com.project.recipe.dto.RecipeResponse;

import com.project.recipe.model.FavoritesDocument;
import com.project.recipe.model.InteractionDocument;
import com.project.recipe.model.RecipeCandidate;
import com.project.recipe.model.RecommendationResult;
import com.project.recipe.model.UserPreference;
import com.project.recipe.model.UserPreferenceDocument;

import com.project.recipe.recommendation.RecommendationService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecommendationOrchestrationService {

    private final PersonalizationService personalizationService;

    private final RecipeService recipeService;

    private final RecommendationService recommendationService;


    public RecommendationOrchestrationService(

            PersonalizationService personalizationService,

            RecipeService recipeService

    ) {

        this.personalizationService =
                personalizationService;

        this.recipeService =
                recipeService;

        this.recommendationService =
                new RecommendationService(
                        RecommendationWeights.defaultWeights()
                );
    }


    public List<RecommendationResult> recommend(

            Long userId,

            List<String> availableIngredients

    ) {

        UserPreferenceDocument preferenceDocument =
                personalizationService
                        .getPreference(userId);


        FavoritesDocument favorites =
                personalizationService
                        .getFavorites(userId);


        List<InteractionDocument> interactions =
                personalizationService
                        .getInteractions(userId);


        List<RecipeResponse> recipes =
                recipeService
                        .getAllRecipes();


        Set<String> positivelyLikedRecipeIds =
                collectPositiveRecipeIds(
                        favorites,
                        interactions
                );


        Set<String> previouslyLikedCategories =
                new HashSet<>();


        for (RecipeResponse recipe : recipes) {

            if (
                    positivelyLikedRecipeIds.contains(
                            recipe.recipeId()
                    )
            ) {

                previouslyLikedCategories.add(
                        recipe.category()
                );
            }
        }


        UserPreference preference =
                new UserPreference(

                        userId,

                        preferenceDocument.getDiet(),

                        safeList(
                                preferenceDocument
                                        .getPreferredCuisines()
                        ),

                        preferenceDocument
                                .getMaxCalories(),

                        safeList(
                                preferenceDocument
                                        .getAllergies()
                        ),

                        safeList(
                                preferenceDocument
                                        .getDislikedIngredients()
                        ),

                        safeList(
                                preferenceDocument
                                        .getFavoriteIngredients()
                        ),

                        previouslyLikedCategories
                );


        List<RecipeCandidate> candidates =
                recipes.stream()
                        .map(
                                this::toCandidate
                        )
                        .toList();


        List<String> ingredients =
                availableIngredients == null
                        ? List.of()
                        : availableIngredients;


        List<RecommendationResult> results =
                recommendationService
                        .recommend(
                                preference,
                                candidates,
                                ingredients
                        );


        personalizationService
                .recordInteraction(

                        userId,

                        null,

                        "RECOMMENDATION_VIEW",

                        null,

                        "RECOMMENDATION",

                        java.util.Map.of(
                                "resultCount",
                                results.size()
                        )
                );


        return results;
    }


    private RecipeCandidate toCandidate(
            RecipeResponse recipe
    ) {

        List<String> ingredients =
                recipe.ingredients()
                        .stream()
                        .map(
                                RecipeResponse
                                        .IngredientResponse::name
                        )
                        .toList();


        return new RecipeCandidate(

                recipe.recipeId(),

                recipe.name(),

                recipe.cuisine(),

                recipe.category(),

                recipe.dietaryType(),

                (int) recipe
                        .nutrition()
                        .calories(),

                recipe.cookingTime(),

                ingredients,

                recipe.allergens()
        );
    }


    private Set<String> collectPositiveRecipeIds(

            FavoritesDocument favorites,

            List<InteractionDocument> interactions

    ) {

        Set<String> recipeIds =
                new HashSet<>();


        recipeIds.addAll(
                safeList(
                        favorites.getRecipeIds()
                )
        );


        for (InteractionDocument interaction : interactions) {

            if (interaction.getRecipeId() == null) {
                continue;
            }


            boolean liked =
                    "LIKE".equalsIgnoreCase(
                            interaction.getAction()
                    );


            boolean highRating =
                    interaction.getRating() != null
                            && interaction.getRating() >= 4;


            boolean favorite =
                    "FAVORITE_ADD".equalsIgnoreCase(
                            interaction.getAction()
                    );


            if (
                    liked
                            || highRating
                            || favorite
            ) {

                recipeIds.add(
                        interaction.getRecipeId()
                );
            }
        }


        return recipeIds;
    }


    private List<String> safeList(
            List<String> values
    ) {

        return values == null
                ? new ArrayList<>()
                : values;
    }
}