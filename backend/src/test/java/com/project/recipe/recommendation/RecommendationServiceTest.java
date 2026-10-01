package com.project.recipe.recommendation;

import com.project.recipe.config.RecommendationWeights;
import com.project.recipe.model.RecipeCandidate;
import com.project.recipe.model.RecommendationResult;
import com.project.recipe.model.UserPreference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationServiceTest {

    private RecommendationService service;

    private UserPreference preference;


    @BeforeEach
    void setUp() {

        service =
                new RecommendationService(
                        RecommendationWeights.defaultWeights()
                );


        preference =
                new UserPreference(

                        102,

                        "Vegetarian",

                        List.of(
                                "Indian",
                                "Italian"
                        ),

                        650,

                        List.of(
                                "Peanut"
                        ),

                        List.of(
                                "Mushroom"
                        ),

                        List.of(
                                "Paneer",
                                "Tomato",
                                "Spinach"
                        ),

                        Set.of(
                                "Main Course"
                        )
                );
    }


    @Test
    void shouldScorePerfectMatchAt100() {

        RecipeCandidate recipe =
                new RecipeCandidate(

                        "R004",
                        "Palak Paneer",
                        "Indian",
                        "Main Course",
                        "Vegetarian",
                        420,
                        30,

                        List.of(
                                "Paneer",
                                "Spinach",
                                "Tomato",
                                "Onion"
                        ),

                        List.of(
                                "Dairy"
                        )
                );


        List<RecommendationResult> results =
                service.recommend(

                        preference,

                        List.of(recipe),

                        List.of(
                                "Paneer",
                                "Tomato",
                                "Spinach"
                        )
                );


        assertEquals(
                1,
                results.size()
        );


        assertEquals(
                100.0,
                results.getFirst().score()
        );
    }


    @Test
    void shouldExcludeNonVegetarianRecipe() {

        RecipeCandidate recipe =
                new RecipeCandidate(

                        "R014",
                        "Chicken Alfredo Pasta",
                        "Italian",
                        "Main Course",
                        "Non-Vegetarian",
                        600,
                        35,

                        List.of(
                                "Chicken",
                                "Cream"
                        ),

                        List.of(
                                "Dairy"
                        )
                );


        List<RecommendationResult> results =
                service.recommend(
                        preference,
                        List.of(recipe),
                        List.of()
                );


        assertTrue(
                results.isEmpty()
        );
    }


    @Test
    void shouldExcludeRecipeContainingUserAllergen() {

        RecipeCandidate recipe =
                new RecipeCandidate(

                        "TEST001",
                        "Peanut Vegetable Bowl",
                        "Indian",
                        "Main Course",
                        "Vegetarian",
                        400,
                        25,

                        List.of(
                                "Vegetables",
                                "Peanut"
                        ),

                        List.of(
                                "Peanut"
                        )
                );


        List<RecommendationResult> results =
                service.recommend(
                        preference,
                        List.of(recipe),
                        List.of()
                );


        assertTrue(
                results.isEmpty()
        );
    }


    @Test
    void shouldExcludeRecipeAboveMaximumCalories() {

        RecipeCandidate recipe =
                new RecipeCandidate(

                        "TEST002",
                        "High Calorie Paneer",
                        "Indian",
                        "Main Course",
                        "Vegetarian",
                        900,
                        40,

                        List.of(
                                "Paneer",
                                "Tomato"
                        ),

                        List.of(
                                "Dairy"
                        )
                );


        List<RecommendationResult> results =
                service.recommend(
                        preference,
                        List.of(recipe),
                        List.of()
                );


        assertTrue(
                results.isEmpty()
        );
    }


    @Test
    void shouldReturnHighestScoringRecipeFirst() {

        RecipeCandidate high =
                new RecipeCandidate(

                        "R004",
                        "Palak Paneer",
                        "Indian",
                        "Main Course",
                        "Vegetarian",
                        420,
                        30,

                        List.of(
                                "Paneer",
                                "Spinach",
                                "Tomato"
                        ),

                        List.of(
                                "Dairy"
                        )
                );


        RecipeCandidate lower =
                new RecipeCandidate(

                        "R011",
                        "Margherita Pizza",
                        "Italian",
                        "Main Course",
                        "Vegetarian",
                        620,
                        30,

                        List.of(
                                "Pizza Dough",
                                "Tomato Sauce",
                                "Mozzarella Cheese"
                        ),

                        List.of(
                                "Dairy",
                                "Gluten"
                        )
                );


        List<RecommendationResult> results =
                service.recommend(

                        preference,

                        List.of(
                                lower,
                                high
                        ),

                        List.of(
                                "Paneer",
                                "Tomato",
                                "Spinach"
                        )
                );


        assertFalse(
                results.isEmpty()
        );


        assertEquals(
                "R004",
                results.getFirst().recipeId()
        );


        assertTrue(
                results.getFirst().score()
                        > results.get(1).score()
        );
    }
}