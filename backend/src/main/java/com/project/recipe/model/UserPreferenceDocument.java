package com.project.recipe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "preferences")
public class UserPreferenceDocument {

    @Id
    private String id;

    private Long userId;

    private String diet;

    private List<String> preferredCuisines;

    private Integer maxCalories;

    private List<String> allergies;

    private List<String> dislikedIngredients;

    private List<String> favoriteIngredients;

    private List<String> preferredDifficulty;

    private Instant updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public List<String> getPreferredCuisines() {
        return preferredCuisines;
    }

    public void setPreferredCuisines(
            List<String> preferredCuisines
    ) {
        this.preferredCuisines = preferredCuisines;
    }

    public Integer getMaxCalories() {
        return maxCalories;
    }

    public void setMaxCalories(Integer maxCalories) {
        this.maxCalories = maxCalories;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public List<String> getDislikedIngredients() {
        return dislikedIngredients;
    }

    public void setDislikedIngredients(
            List<String> dislikedIngredients
    ) {
        this.dislikedIngredients = dislikedIngredients;
    }

    public List<String> getFavoriteIngredients() {
        return favoriteIngredients;
    }

    public void setFavoriteIngredients(
            List<String> favoriteIngredients
    ) {
        this.favoriteIngredients = favoriteIngredients;
    }

    public List<String> getPreferredDifficulty() {
        return preferredDifficulty;
    }

    public void setPreferredDifficulty(
            List<String> preferredDifficulty
    ) {
        this.preferredDifficulty = preferredDifficulty;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}