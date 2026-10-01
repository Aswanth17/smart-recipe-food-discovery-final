package com.project.recipe.model;

import java.util.List;

public record RecommendationResult(

        String recipeId,

        String recipeName,

        double score,

        List<String> reasons

) {

    public RecommendationResult {

        reasons =
                reasons == null
                        ? List.of()
                        : List.copyOf(reasons);
    }
}