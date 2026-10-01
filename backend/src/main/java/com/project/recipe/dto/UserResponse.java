package com.project.recipe.dto;

public record UserResponse(

        Long userId,

        String fullName,

        String email,

        String role,

        boolean active

) {
}