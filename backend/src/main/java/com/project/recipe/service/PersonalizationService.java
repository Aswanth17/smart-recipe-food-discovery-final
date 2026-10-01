package com.project.recipe.service;

import com.project.recipe.model.FavoritesDocument;
import com.project.recipe.model.InteractionDocument;
import com.project.recipe.model.UserPreferenceDocument;

import com.project.recipe.repository.FavoritesRepository;
import com.project.recipe.repository.InteractionRepository;
import com.project.recipe.repository.UserPreferenceRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PersonalizationService {

    private final UserPreferenceRepository preferenceRepository;

    private final FavoritesRepository favoritesRepository;

    private final InteractionRepository interactionRepository;


    public PersonalizationService(

            UserPreferenceRepository preferenceRepository,

            FavoritesRepository favoritesRepository,

            InteractionRepository interactionRepository

    ) {

        this.preferenceRepository =
                preferenceRepository;

        this.favoritesRepository =
                favoritesRepository;

        this.interactionRepository =
                interactionRepository;
    }


    public UserPreferenceDocument getPreference(
            Long userId
    ) {

        return preferenceRepository
                .findByUserId(userId)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException(
                                        "Preferences not found for user: "
                                                + userId
                                )
                );
    }


    public UserPreferenceDocument updatePreference(

            Long userId,

            UserPreferenceDocument updated

    ) {

        UserPreferenceDocument existing =
                preferenceRepository
                        .findByUserId(userId)
                        .orElseGet(
                                UserPreferenceDocument::new
                        );


        existing.setUserId(userId);

        existing.setDiet(
                updated.getDiet()
        );

        existing.setPreferredCuisines(
                safeList(
                        updated.getPreferredCuisines()
                )
        );

        existing.setMaxCalories(
                updated.getMaxCalories()
        );

        existing.setAllergies(
                safeList(
                        updated.getAllergies()
                )
        );

        existing.setDislikedIngredients(
                safeList(
                        updated.getDislikedIngredients()
                )
        );

        existing.setFavoriteIngredients(
                safeList(
                        updated.getFavoriteIngredients()
                )
        );

        existing.setPreferredDifficulty(
                safeList(
                        updated.getPreferredDifficulty()
                )
        );

        existing.setUpdatedAt(
                Instant.now()
        );


        return preferenceRepository.save(
                existing
        );
    }


    public FavoritesDocument getFavorites(
            Long userId
    ) {

        return favoritesRepository
                .findByUserId(userId)
                .orElseGet(
                        () -> {

                            FavoritesDocument document =
                                    new FavoritesDocument();

                            document.setUserId(userId);

                            document.setRecipeIds(
                                    new ArrayList<>()
                            );

                            document.setUpdatedAt(
                                    Instant.now()
                            );

                            return favoritesRepository.save(
                                    document
                            );
                        }
                );
    }


    public FavoritesDocument addFavorite(

            Long userId,

            String recipeId

    ) {

        FavoritesDocument document =
                getFavorites(userId);


        List<String> recipes =
                new ArrayList<>(
                        safeList(
                                document.getRecipeIds()
                        )
                );


        if (!recipes.contains(recipeId)) {

            recipes.add(recipeId);
        }


        document.setRecipeIds(recipes);

        document.setUpdatedAt(
                Instant.now()
        );


        FavoritesDocument saved =
                favoritesRepository.save(
                        document
                );


        recordInteraction(

                userId,

                recipeId,

                "FAVORITE_ADD",

                null,

                "RECIPE_DETAILS",

                Map.of()
        );


        return saved;
    }


    public FavoritesDocument removeFavorite(

            Long userId,

            String recipeId

    ) {

        FavoritesDocument document =
                getFavorites(userId);


        List<String> recipes =
                new ArrayList<>(
                        safeList(
                                document.getRecipeIds()
                        )
                );


        recipes.remove(recipeId);


        document.setRecipeIds(
                recipes
        );

        document.setUpdatedAt(
                Instant.now()
        );


        FavoritesDocument saved =
                favoritesRepository.save(
                        document
                );


        recordInteraction(

                userId,

                recipeId,

                "FAVORITE_REMOVE",

                null,

                "RECIPE_DETAILS",

                Map.of()
        );


        return saved;
    }


    public List<InteractionDocument> getInteractions(
            Long userId
    ) {

        return interactionRepository
                .findByUserIdOrderByCreatedAtDesc(
                        userId
                );
    }


    public void recordInteraction(

            Long userId,

            String recipeId,

            String action,

            Integer rating,

            String source,

            Map<String, Object> metadata

    ) {

        InteractionDocument document =
                new InteractionDocument();


        document.setUserId(userId);

        document.setRecipeId(recipeId);

        document.setAction(action);

        document.setRating(rating);

        document.setSource(source);

        document.setMetadata(
                metadata == null
                        ? Map.of()
                        : metadata
        );

        document.setCreatedAt(
                Instant.now()
        );


        interactionRepository.save(
                document
        );
    }


    private List<String> safeList(
            List<String> values
    ) {

        return values == null
                ? List.of()
                : values;
    }
}