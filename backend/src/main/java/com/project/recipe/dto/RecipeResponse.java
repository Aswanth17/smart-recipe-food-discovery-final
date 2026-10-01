package com.project.recipe.dto;

import java.util.List;

public record RecipeResponse(

        String recipeId,

        String name,

        String description,

        String cuisine,

        String category,

        List<IngredientResponse> ingredients,

        List<String> steps,

        NutritionResponse nutrition,

        int cookingTime,

        String difficulty,

        String dietaryType,

        List<String> allergens,

        List<String> tags

) {

    public record IngredientResponse(
            String name,
            double quantity,
            String unit
    ) {
    }

    public record NutritionResponse(
            double calories,
            double protein,
            double carbohydrates,
            double fat
    ) {
    }
}