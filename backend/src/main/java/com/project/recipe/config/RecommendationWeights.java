package com.project.recipe.config;

public final class RecommendationWeights {

    private final int preferredCuisine;
    private final int dietCompatibility;
    private final int availableIngredients;
    private final int favoriteIngredient;
    private final int previouslyLikedType;

    public RecommendationWeights(
            int preferredCuisine,
            int dietCompatibility,
            int availableIngredients,
            int favoriteIngredient,
            int previouslyLikedType
    ) {

        if (preferredCuisine < 0
                || dietCompatibility < 0
                || availableIngredients < 0
                || favoriteIngredient < 0
                || previouslyLikedType < 0) {

            throw new IllegalArgumentException(
                    "Recommendation weights cannot be negative."
            );
        }

        this.preferredCuisine = preferredCuisine;
        this.dietCompatibility = dietCompatibility;
        this.availableIngredients = availableIngredients;
        this.favoriteIngredient = favoriteIngredient;
        this.previouslyLikedType = previouslyLikedType;
    }

    public static RecommendationWeights defaultWeights() {
        return new RecommendationWeights(
                30,
                30,
                20,
                10,
                10
        );
    }

    public int getPreferredCuisine() {
        return preferredCuisine;
    }

    public int getDietCompatibility() {
        return dietCompatibility;
    }

    public int getAvailableIngredients() {
        return availableIngredients;
    }

    public int getFavoriteIngredient() {
        return favoriteIngredient;
    }

    public int getPreviouslyLikedType() {
        return previouslyLikedType;
    }

    public int getMaximumScore() {
        return preferredCuisine
                + dietCompatibility
                + availableIngredients
                + favoriteIngredient
                + previouslyLikedType;
    }
}