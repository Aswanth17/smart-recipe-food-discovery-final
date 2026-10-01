package com.project.recipe.recommendation;

import com.project.recipe.config.RecommendationWeights;
import com.project.recipe.model.RecipeCandidate;
import com.project.recipe.model.RecommendationResult;
import com.project.recipe.model.UserPreference;

import java.util.List;
import java.util.Set;

public class RecommendationEngineDemo {

    public static void main(String[] args) {

        RecommendationWeights weights =
                RecommendationWeights.defaultWeights();


        RecommendationService service =
                new RecommendationService(
                        weights
                );


        /*
         * Demo user similar to the MongoDB
         * vegetarian preference profile.
         */

        UserPreference user =
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


        /*
         * Ingredients currently available
         * to the user.
         */

        List<String> availableIngredients =
                List.of(
                        "Paneer",
                        "Tomato",
                        "Spinach"
                );


        /*
         * Sample candidates use recipes already
         * present in recipes.xml.
         */

        List<RecipeCandidate> candidates =
                List.of(

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
                                        "Onion",
                                        "Tomato",
                                        "Garlic"
                                ),
                                List.of(
                                        "Dairy"
                                )
                        ),


                        new RecipeCandidate(
                                "R002",
                                "Paneer Butter Masala",
                                "Indian",
                                "Main Course",
                                "Vegetarian",
                                520,
                                35,
                                List.of(
                                        "Paneer",
                                        "Tomato",
                                        "Butter",
                                        "Fresh Cream",
                                        "Onion"
                                ),
                                List.of(
                                        "Dairy"
                                )
                        ),


                        new RecipeCandidate(
                                "R027",
                                "Paneer Tikka",
                                "Indian",
                                "Starter",
                                "Vegetarian",
                                430,
                                30,
                                List.of(
                                        "Paneer",
                                        "Yogurt",
                                        "Capsicum",
                                        "Onion"
                                ),
                                List.of(
                                        "Dairy"
                                )
                        ),


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
                                        "Mozzarella Cheese",
                                        "Basil"
                                ),
                                List.of(
                                        "Gluten",
                                        "Dairy"
                                )
                        ),


                        /*
                         * This recipe exceeds maxCalories=650,
                         * so it will be excluded automatically.
                         */

                        new RecipeCandidate(
                                "R014",
                                "Chicken Alfredo Pasta",
                                "Italian",
                                "Main Course",
                                "Non-Vegetarian",
                                760,
                                35,
                                List.of(
                                        "Fettuccine",
                                        "Chicken",
                                        "Fresh Cream",
                                        "Parmesan Cheese",
                                        "Butter"
                                ),
                                List.of(
                                        "Dairy",
                                        "Gluten"
                                )
                        ),


                        /*
                         * Peanut allergy makes this
                         * automatically ineligible.
                         */

                        new RecipeCandidate(
                                "R026",
                                "Kung Pao Chicken",
                                "Chinese",
                                "Main Course",
                                "Non-Vegetarian",
                                580,
                                30,
                                List.of(
                                        "Chicken",
                                        "Peanut",
                                        "Capsicum",
                                        "Soy Sauce",
                                        "Dried Chilli"
                                ),
                                List.of(
                                        "Peanut",
                                        "Soy"
                                )
                        )
                );


        List<RecommendationResult> results =
                service.recommend(
                        user,
                        candidates,
                        availableIngredients
                );


        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                " SMART RECIPE RECOMMENDATION ENGINE"
        );

        System.out.println(
                "=============================================="
        );


        System.out.println(
                "User ID: "
                        + user.userId()
        );

        System.out.println(
                "Diet: "
                        + user.diet()
        );

        System.out.println(
                "Preferred cuisines: "
                        + user.preferredCuisines()
        );

        System.out.println(
                "Available ingredients: "
                        + availableIngredients
        );

        System.out.println(
                "Maximum possible score: "
                        + weights.getMaximumScore()
        );


        System.out.println();

        System.out.println(
                "RECOMMENDED FOR YOU"
        );

        System.out.println(
                "----------------------------------------------"
        );


        int rank = 1;


        for (RecommendationResult result : results) {

            System.out.printf(
                    "%d. %s%n",
                    rank,
                    result.recipeName()
            );

            System.out.printf(
                    "   Recipe ID : %s%n",
                    result.recipeId()
            );

            System.out.printf(
                    "   Score     : %.2f / %d%n",
                    result.score(),
                    weights.getMaximumScore()
            );

            System.out.println(
                    "   Reasons:"
            );


            for (String reason : result.reasons()) {

                System.out.println(
                        "     - " + reason
                );
            }


            System.out.println();

            rank++;
        }


        System.out.println(
                "=============================================="
        );
    }
}