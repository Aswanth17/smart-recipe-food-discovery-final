package com.project.recipe.model;

import java.util.List;

public record RecipeCandidate(

        String recipeId,

        String name,

        String cuisine,

        String category,

        String dietaryType,

        int calories,

        int cookingTime,

        List<String> ingredients,

        List<String> allergens

) {

    public RecipeCandidate {

        if (recipeId == null || recipeId.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe ID cannot be empty."
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipe name cannot be empty."
            );
        }

        ingredients =
                ingredients == null
                        ? List.of()
                        : List.copyOf(ingredients);

        allergens =
                allergens == null
                        ? List.of()
                        : List.copyOf(allergens);
    }
}