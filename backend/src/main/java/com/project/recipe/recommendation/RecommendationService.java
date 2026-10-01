package com.project.recipe.recommendation;

import com.project.recipe.config.RecommendationWeights;
import com.project.recipe.model.RecipeCandidate;
import com.project.recipe.model.RecommendationResult;
import com.project.recipe.model.UserPreference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class RecommendationService {

    private final RecommendationWeights weights;

    public RecommendationService(
            RecommendationWeights weights
    ) {

        if (weights == null) {
            throw new IllegalArgumentException(
                    "Recommendation weights are required."
            );
        }

        this.weights = weights;
    }


    public List<RecommendationResult> recommend(

            UserPreference preference,

            List<RecipeCandidate> candidates,

            List<String> availableIngredients

    ) {

        if (preference == null) {
            throw new IllegalArgumentException(
                    "User preference is required."
            );
        }

        if (candidates == null) {
            throw new IllegalArgumentException(
                    "Recipe candidates are required."
            );
        }

        List<String> available =
                availableIngredients == null
                        ? List.of()
                        : availableIngredients;

        return candidates.stream()

                .map(recipe ->
                        scoreRecipe(
                                preference,
                                recipe,
                                available
                        )
                )

                .flatMap(Optional::stream)

                .sorted(
                        Comparator
                                .comparingDouble(
                                        RecommendationResult::score
                                )
                                .reversed()
                                .thenComparing(
                                        RecommendationResult::recipeName
                                )
                )

                .toList();
    }


    private Optional<RecommendationResult> scoreRecipe(

            UserPreference preference,

            RecipeCandidate recipe,

            List<String> availableIngredients

    ) {

        /*
         * Hard exclusion rules.
         *
         * These are intentionally NOT score deductions.
         * A recipe that violates a dietary restriction,
         * allergy, disliked ingredient, or calorie limit
         * should not be recommended.
         */

        if (!isDietCompatible(
                preference.diet(),
                recipe.dietaryType()
        )) {

            return Optional.empty();
        }


        if (containsAllergen(
                recipe.allergens(),
                preference.allergies()
        )) {

            return Optional.empty();
        }


        if (containsDislikedIngredient(
                recipe.ingredients(),
                preference.dislikedIngredients()
        )) {

            return Optional.empty();
        }


        if (preference.maxCalories() != null
                && recipe.calories() > preference.maxCalories()) {

            return Optional.empty();
        }


        double score = 0.0;

        List<String> reasons = new ArrayList<>();


        // =====================================================
        // RULE 1:
        // Preferred cuisine
        // Maximum: +30
        // =====================================================

        if (containsIgnoreCase(
                preference.preferredCuisines(),
                recipe.cuisine()
        )) {

            score += weights.getPreferredCuisine();

            reasons.add(
                    "Matches preferred cuisine: "
                            + recipe.cuisine()
            );
        }


        // =====================================================
        // RULE 2:
        // Diet compatibility
        // Maximum: +30
        // =====================================================

        if (isDietCompatible(
                preference.diet(),
                recipe.dietaryType()
        )) {

            score += weights.getDietCompatibility();

            if (preference.diet() == null
                    || preference.diet().isBlank()) {

                reasons.add(
                        "Compatible with user's unrestricted diet"
                );

            } else {

                reasons.add(
                        "Compatible with "
                                + preference.diet()
                                + " diet"
                );
            }
        }


        // =====================================================
        // RULE 3:
        // Available ingredient match
        // Maximum: +20
        //
        // The score is proportional.
        //
        // Example:
        //
        // Available:
        // Paneer, Tomato, Spinach
        //
        // Recipe contains:
        // Paneer + Tomato
        //
        // Match:
        // 2 / 3
        //
        // Ingredient score:
        // 20 * (2/3)
        // =====================================================

        IngredientMatch ingredientMatch =
                calculateIngredientMatch(
                        recipe.ingredients(),
                        availableIngredients
                );

        if (ingredientMatch.requestedCount() > 0
                && ingredientMatch.matchedCount() > 0) {

            double ingredientScore =
                    weights.getAvailableIngredients()
                            * ingredientMatch.matchRatio();

            score += ingredientScore;

            reasons.add(
                    "Matches "
                            + ingredientMatch.matchedCount()
                            + " of "
                            + ingredientMatch.requestedCount()
                            + " available ingredients"
            );
        }


        // =====================================================
        // RULE 4:
        // Favorite ingredient
        // Maximum: +10
        // =====================================================

        Optional<String> favoriteIngredient =
                firstMatchingIngredient(
                        recipe.ingredients(),
                        preference.favoriteIngredients()
                );

        if (favoriteIngredient.isPresent()) {

            score += weights.getFavoriteIngredient();

            reasons.add(
                    "Contains favorite ingredient: "
                            + favoriteIngredient.get()
            );
        }


        // =====================================================
        // RULE 5:
        // Previously liked recipe type/category
        // Maximum: +10
        // =====================================================

        if (containsIgnoreCase(
                preference.previouslyLikedCategories(),
                recipe.category()
        )) {

            score += weights.getPreviouslyLikedType();

            reasons.add(
                    "Matches a previously liked recipe category: "
                            + recipe.category()
            );
        }


        /*
         * Defensive cap.
         *
         * Even if scoring logic changes later,
         * the result cannot exceed configured maximum.
         */

        score = Math.min(
                score,
                weights.getMaximumScore()
        );


        score = roundToTwoDecimals(score);


        return Optional.of(
                new RecommendationResult(
                        recipe.recipeId(),
                        recipe.name(),
                        score,
                        reasons
                )
        );
    }


    private boolean isDietCompatible(

            String userDiet,

            String recipeDiet

    ) {

        if (userDiet == null
                || userDiet.isBlank()) {

            return true;
        }

        if (recipeDiet == null
                || recipeDiet.isBlank()) {

            return false;
        }


        String user =
                normalize(userDiet);

        String recipe =
                normalize(recipeDiet);


        return switch (user) {

            /*
             * A vegan user receives only vegan recipes.
             */

            case "vegan" ->
                    recipe.equals("vegan");


            /*
             * Vegetarian users may receive Vegetarian
             * or Vegan recipes.
             */

            case "vegetarian" ->
                    recipe.equals("vegetarian")
                            || recipe.equals("vegan");


            /*
             * Non-vegetarian users have no meat restriction.
             * Therefore Vegetarian/Vegan food remains compatible.
             */

            case "non-vegetarian" ->
                    recipe.equals("non-vegetarian")
                            || recipe.equals("vegetarian")
                            || recipe.equals("vegan");


            default ->
                    recipe.equals(user);
        };
    }


    private boolean containsAllergen(

            List<String> recipeAllergens,

            List<String> userAllergies

    ) {

        if (userAllergies == null
                || userAllergies.isEmpty()) {

            return false;
        }

        Set<String> allergies =
                normalizeToSet(userAllergies);

        return recipeAllergens.stream()

                .map(this::normalize)

                .filter(value ->
                        !value.equals("none")
                )

                .anyMatch(allergies::contains);
    }


    private boolean containsDislikedIngredient(

            List<String> recipeIngredients,

            List<String> dislikedIngredients

    ) {

        if (dislikedIngredients == null
                || dislikedIngredients.isEmpty()) {

            return false;
        }

        Set<String> disliked =
                normalizeToSet(dislikedIngredients);

        return recipeIngredients.stream()
                .map(this::normalize)
                .anyMatch(disliked::contains);
    }


    private IngredientMatch calculateIngredientMatch(

            List<String> recipeIngredients,

            List<String> availableIngredients

    ) {

        if (availableIngredients == null
                || availableIngredients.isEmpty()) {

            return new IngredientMatch(
                    0,
                    0
            );
        }


        Set<String> requested =
                normalizeToSet(
                        availableIngredients
                );


        Set<String> recipeIngredientSet =
                normalizeToSet(
                        recipeIngredients
                );


        long matched =
                requested.stream()
                        .filter(
                                recipeIngredientSet::contains
                        )
                        .count();


        return new IngredientMatch(
                requested.size(),
                (int) matched
        );
    }


    private Optional<String> firstMatchingIngredient(

            List<String> recipeIngredients,

            List<String> favoriteIngredients

    ) {

        if (favoriteIngredients == null
                || favoriteIngredients.isEmpty()) {

            return Optional.empty();
        }


        Set<String> favoriteSet =
                normalizeToSet(
                        favoriteIngredients
                );


        return recipeIngredients.stream()

                .filter(ingredient ->
                        favoriteSet.contains(
                                normalize(ingredient)
                        )
                )

                .findFirst();
    }


    private boolean containsIgnoreCase(

            Iterable<String> values,

            String expected

    ) {

        if (values == null
                || expected == null) {

            return false;
        }


        String normalizedExpected =
                normalize(expected);


        for (String value : values) {

            if (normalize(value)
                    .equals(normalizedExpected)) {

                return true;
            }
        }

        return false;
    }


    private Set<String> normalizeToSet(
            List<String> values
    ) {

        Set<String> normalized =
                new HashSet<>();

        if (values == null) {
            return normalized;
        }


        for (String value : values) {

            if (value != null
                    && !value.isBlank()) {

                normalized.add(
                        normalize(value)
                );
            }
        }

        return normalized;
    }


    private String normalize(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .toLowerCase(
                        Locale.ROOT
                );
    }


    private double roundToTwoDecimals(
            double value
    ) {

        return Math.round(value * 100.0)
                / 100.0;
    }


    private record IngredientMatch(

            int requestedCount,

            int matchedCount

    ) {

        double matchRatio() {

            if (requestedCount == 0) {
                return 0.0;
            }

            return (double) matchedCount
                    / requestedCount;
        }
    }
}