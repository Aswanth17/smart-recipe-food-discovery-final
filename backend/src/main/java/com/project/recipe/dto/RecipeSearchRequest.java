package com.project.recipe.dto;

import jakarta.validation.constraints.Min;

import java.util.List;

public record RecipeSearchRequest(

        List<String> ingredients,

        String cuisine,

        String diet,

        @Min(value = 0, message = "Maximum calories cannot be negative.")
        Integer maxCalories,

        @Min(value = 0, message = "Maximum cooking time cannot be negative.")
        Integer maxCookingTime,

        String difficulty

) {

    public RecipeSearchRequest {

        ingredients =
                ingredients == null
                        ? List.of()
                        : List.copyOf(ingredients);
    }
}