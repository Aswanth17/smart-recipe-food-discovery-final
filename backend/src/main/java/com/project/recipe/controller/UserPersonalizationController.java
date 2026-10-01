package com.project.recipe.controller;

import com.project.recipe.dto.FavoriteRequest;

import com.project.recipe.model.FavoritesDocument;
import com.project.recipe.model.InteractionDocument;
import com.project.recipe.model.UserPreferenceDocument;

import com.project.recipe.service.PersonalizationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserPersonalizationController {

    private final PersonalizationService personalizationService;


    public UserPersonalizationController(
            PersonalizationService personalizationService
    ) {

        this.personalizationService =
                personalizationService;
    }


    @GetMapping("/{id}/preferences")
    public UserPreferenceDocument getPreferences(
            @PathVariable Long id
    ) {

        return personalizationService
                .getPreference(id);
    }


    @PutMapping("/{id}/preferences")
    public UserPreferenceDocument updatePreferences(

            @PathVariable Long id,

            @RequestBody
            UserPreferenceDocument request

    ) {

        return personalizationService
                .updatePreference(
                        id,
                        request
                );
    }


    @GetMapping("/{id}/favorites")
    public FavoritesDocument getFavorites(
            @PathVariable Long id
    ) {

        return personalizationService
                .getFavorites(id);
    }


    @PostMapping("/{id}/favorites")
    public FavoritesDocument addFavorite(

            @PathVariable Long id,

            @Valid
            @RequestBody
            FavoriteRequest request

    ) {

        return personalizationService
                .addFavorite(
                        id,
                        request.recipeId()
                );
    }


    @DeleteMapping("/{id}/favorites/{recipeId}")
    public FavoritesDocument removeFavorite(

            @PathVariable Long id,

            @PathVariable String recipeId

    ) {

        return personalizationService
                .removeFavorite(
                        id,
                        recipeId
                );
    }


    @GetMapping("/{id}/interactions")
    public List<InteractionDocument> getInteractions(
            @PathVariable Long id
    ) {

        return personalizationService
                .getInteractions(id);
    }
}