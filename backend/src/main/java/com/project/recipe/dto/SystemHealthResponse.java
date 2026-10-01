package com.project.recipe.dto;

public record SystemHealthResponse(

        String application,

        String postgres,

        String postgis,

        int postgresUsers,

        int restaurants,

        String mongodb,

        long mongoPreferences,

        long mongoFavorites,

        long mongoInteractions,

        String basex,

        int basexRecipes

) {
}