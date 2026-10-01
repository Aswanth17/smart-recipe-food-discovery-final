package com.project.recipe.dto;

import jakarta.validation.constraints.NotBlank;

public record FavoriteRequest(

        @NotBlank(
                message = "Recipe ID is required."
        )
        String recipeId

) {
}