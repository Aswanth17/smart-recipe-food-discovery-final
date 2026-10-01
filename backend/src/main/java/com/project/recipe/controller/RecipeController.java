package com.project.recipe.controller;

import com.project.recipe.dto.RecipeResponse;
import com.project.recipe.dto.RecipeSearchRequest;

import com.project.recipe.service.RecipeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;


    public RecipeController(
            RecipeService recipeService
    ) {

        this.recipeService =
                recipeService;
    }


    @GetMapping
    public List<RecipeResponse> getAllRecipes() {

        return recipeService
                .getAllRecipes();
    }


    @GetMapping("/{id}")
    public RecipeResponse getRecipeById(
            @PathVariable("id")
            String id
    ) {

        return recipeService
                .getRecipeById(id);
    }


    @PostMapping("/search")
    public List<RecipeResponse> searchRecipes(

            @Valid
            @RequestBody
            RecipeSearchRequest request

    ) {

        return recipeService
                .searchRecipes(request);
    }
}