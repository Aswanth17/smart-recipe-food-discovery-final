package com.project.recipe.service;

import com.project.recipe.dto.RecipeResponse;
import com.project.recipe.dto.RecipeSearchRequest;

import com.project.recipe.xml.BaseXClientService;
import com.project.recipe.xml.RecipeXmlParser;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeService {

    private final BaseXClientService baseXClientService;

    private final RecipeXmlParser recipeXmlParser;


    public RecipeService(

            BaseXClientService baseXClientService,

            RecipeXmlParser recipeXmlParser

    ) {

        this.baseXClientService =
                baseXClientService;

        this.recipeXmlParser =
                recipeXmlParser;
    }


    public List<RecipeResponse> getAllRecipes() {

        String database =
                baseXClientService
                        .getDatabaseName();


        String xquery =
                """
                for $recipe in db:get('%s')/recipes/recipe
                order by string($recipe/name)
                return $recipe
                """
                        .formatted(
                                database
                        );


        String xml =
                baseXClientService
                        .executeQuery(
                                xquery
                        );


        return recipeXmlParser
                .parseRecipes(xml);
    }


    public RecipeResponse getRecipeById(
            String recipeId
    ) {

        String database =
                baseXClientService
                        .getDatabaseName();


        String xquery =
                """
                declare variable $recipeId external;

                for $recipe in db:get('%s')/recipes/recipe
                where string($recipe/@id) = $recipeId
                return $recipe
                """
                        .formatted(
                                database
                        );


        String xml =
                baseXClientService
                        .executeQuery(

                                xquery,

                                Map.of(
                                        "recipeId",
                                        recipeId
                                )
                        );


        List<RecipeResponse> recipes =
                recipeXmlParser
                        .parseRecipes(xml);


        if (recipes.isEmpty()) {

            throw new RecipeNotFoundException(
                    recipeId
            );
        }


        return recipes.getFirst();
    }


    public List<RecipeResponse> searchRecipes(
            RecipeSearchRequest request
    ) {

        String database =
                baseXClientService
                        .getDatabaseName();


        String xquery =
                """
                declare variable $ingredients external;
                declare variable $cuisine external;
                declare variable $diet external;
                declare variable $maxCalories external;
                declare variable $maxTime external;
                declare variable $difficulty external;

                let $ingredientList :=
                    for $item in tokenize($ingredients, ",")
                    let $clean := normalize-space($item)
                    where $clean != ""
                    return lower-case($clean)

                for $recipe in db:get('%s')/recipes/recipe

                let $recipeIngredients :=
                    for $name in $recipe/ingredients/ingredient/name
                    return lower-case(
                        normalize-space(
                            string($name)
                        )
                    )

                let $ingredientMatch :=
                    if (
                        empty($ingredientList)
                    )
                    then true()

                    else every $requested
                         in $ingredientList

                         satisfies
                         some $available
                         in $recipeIngredients

                         satisfies
                         (
                             $available = $requested

                             or contains(
                                 $available,
                                 $requested
                             )

                             or contains(
                                 $requested,
                                 $available
                             )
                         )


                let $cuisineMatch :=
                    if (
                        normalize-space(
                            $cuisine
                        ) = ""
                    )
                    then true()

                    else lower-case(
                        string(
                            $recipe/cuisine
                        )
                    )
                    =
                    lower-case(
                        $cuisine
                    )


                let $dietMatch :=
                    if (
                        normalize-space(
                            $diet
                        ) = ""
                    )
                    then true()

                    else lower-case(
                        string(
                            $recipe/dietaryType
                        )
                    )
                    =
                    lower-case(
                        $diet
                    )


                let $calorieMatch :=
                    if (
                        normalize-space(
                            $maxCalories
                        ) = ""
                    )
                    then true()

                    else xs:decimal(
                        $recipe/nutrition/calories
                    )
                    <=
                    xs:decimal(
                        $maxCalories
                    )


                let $timeMatch :=
                    if (
                        normalize-space(
                            $maxTime
                        ) = ""
                    )
                    then true()

                    else xs:integer(
                        $recipe/cookingTime
                    )
                    <=
                    xs:integer(
                        $maxTime
                    )


                let $difficultyMatch :=
                    if (
                        normalize-space(
                            $difficulty
                        ) = ""
                    )
                    then true()

                    else lower-case(
                        string(
                            $recipe/difficulty
                        )
                    )
                    =
                    lower-case(
                        $difficulty
                    )


                where
                    $ingredientMatch
                    and $cuisineMatch
                    and $dietMatch
                    and $calorieMatch
                    and $timeMatch
                    and $difficultyMatch


                order by
                    xs:integer(
                        $recipe/cookingTime
                    ),

                    xs:decimal(
                        $recipe/nutrition/calories
                    )


                return $recipe
                """
                        .formatted(
                                database
                        );


        Map<String, String> variables =
                new HashMap<>();


        variables.put(
                "ingredients",
                String.join(
                        ",",
                        request.ingredients()
                )
        );


        variables.put(
                "cuisine",
                emptyIfNull(
                        request.cuisine()
                )
        );


        variables.put(
                "diet",
                emptyIfNull(
                        request.diet()
                )
        );


        variables.put(
                "maxCalories",
                request.maxCalories() == null
                        ? ""
                        : request.maxCalories()
                        .toString()
        );


        variables.put(
                "maxTime",
                request.maxCookingTime() == null
                        ? ""
                        : request.maxCookingTime()
                        .toString()
        );


        variables.put(
                "difficulty",
                emptyIfNull(
                        request.difficulty()
                )
        );


        String xml =
                baseXClientService
                        .executeQuery(
                                xquery,
                                variables
                        );


        return recipeXmlParser
                .parseRecipes(xml);
    }


    private String emptyIfNull(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }


    public static class RecipeNotFoundException
            extends RuntimeException {

        public RecipeNotFoundException(
                String recipeId
        ) {

            super(
                    "Recipe not found: "
                            + recipeId
            );
        }
    }
}