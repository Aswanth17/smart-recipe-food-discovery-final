package com.project.recipe.controller;

import com.project.recipe.dto.UserResponse;

import com.project.recipe.service.UserProfileService;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserProfileService profileService;


    public ProfileController(
            UserProfileService profileService
    ) {

        this.profileService =
                profileService;
    }


    @GetMapping
    public UserResponse getProfile(
            Authentication authentication
    ) {

        Long userId =
                (Long)
                        authentication
                                .getPrincipal();


        return profileService
                .getProfile(
                        userId
                );
    }
}