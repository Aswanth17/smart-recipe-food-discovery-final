package com.project.recipe.dto;

public record AuthResponse(

        String token,

        String tokenType,

        UserResponse user

) {
}