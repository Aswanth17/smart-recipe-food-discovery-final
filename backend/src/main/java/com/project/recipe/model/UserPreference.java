package com.project.recipe.model;

import java.util.List;
import java.util.Set;

public record UserPreference(

        long userId,

        String diet,

        List<String> preferredCuisines,

        Integer maxCalories,

        List<String> allergies,

        List<String> dislikedIngredients,

        List<String> favoriteIngredients,

        Set<String> previouslyLikedCategories

) {

    public UserPreference {

        preferredCuisines =
                preferredCuisines == null
                        ? List.of()
                        : List.copyOf(preferredCuisines);

        allergies =
                allergies == null
                        ? List.of()
                        : List.copyOf(allergies);

        dislikedIngredients =
                dislikedIngredients == null
                        ? List.of()
                        : List.copyOf(dislikedIngredients);

        favoriteIngredients =
                favoriteIngredients == null
                        ? List.of()
                        : List.copyOf(favoriteIngredients);

        previouslyLikedCategories =
                previouslyLikedCategories == null
                        ? Set.of()
                        : Set.copyOf(previouslyLikedCategories);
    }
}