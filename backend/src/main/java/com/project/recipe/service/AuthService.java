package com.project.recipe.service;

import com.project.recipe.dto.AuthResponse;
import com.project.recipe.dto.LoginRequest;
import com.project.recipe.dto.RegisterRequest;
import com.project.recipe.dto.UserResponse;

import com.project.recipe.model.UserPreferenceDocument;

import com.project.recipe.repository.UserJdbcRepository;
import com.project.recipe.repository.UserPreferenceRepository;

import com.project.recipe.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserJdbcRepository userRepository;

    private final UserPreferenceRepository preferenceRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    public AuthService(

            UserJdbcRepository userRepository,

            UserPreferenceRepository preferenceRepository,

            PasswordEncoder passwordEncoder,

            JwtService jwtService

    ) {

        this.userRepository =
                userRepository;

        this.preferenceRepository =
                preferenceRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.jwtService =
                jwtService;
    }


    public AuthResponse register(
            RegisterRequest request
    ) {

        if (
                userRepository
                        .findByEmail(
                                request.email()
                        )
                        .isPresent()
        ) {

            throw new IllegalArgumentException(
                    "An account already exists with this email."
            );
        }


        String passwordHash =
                passwordEncoder.encode(
                        request.password()
                );


        Long userId =
                userRepository.createUser(

                        request.fullName(),

                        request.email()
                                .trim()
                                .toLowerCase(),

                        passwordHash
                );


        createDefaultPreferences(
                userId
        );


        UserResponse user =
                new UserResponse(

                        userId,

                        request.fullName(),

                        request.email()
                                .trim()
                                .toLowerCase(),

                        "USER",

                        true
                );


        String token =
                jwtService.generateToken(

                        userId,

                        user.email(),

                        user.role()
                );


        return new AuthResponse(

                token,

                "Bearer",

                user
        );
    }


    public AuthResponse login(
            LoginRequest request
    ) {

        Map<String, Object> row =
                userRepository
                        .findByEmail(
                                request.email()
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Invalid email or password."
                                        )
                        );


        boolean active =
                Boolean.TRUE.equals(
                        row.get("is_active")
                );


        if (!active) {

            throw new IllegalArgumentException(
                    "This account is inactive."
            );
        }


        String passwordHash =
                row.get(
                        "password_hash"
                )
                        .toString();


        if (
                !passwordEncoder.matches(
                        request.password(),
                        passwordHash
                )
        ) {

            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }


        Long userId =
                ((Number)
                        row.get("user_id"))
                        .longValue();


        String fullName =
                row.get(
                        "full_name"
                )
                        .toString();


        String email =
                row.get(
                        "email"
                )
                        .toString();


        String role =
                row.get(
                        "role"
                )
                        .toString();


        UserResponse user =
                new UserResponse(

                        userId,

                        fullName,

                        email,

                        role,

                        true
                );


        String token =
                jwtService.generateToken(

                        userId,

                        email,

                        role
                );


        return new AuthResponse(

                token,

                "Bearer",

                user
        );
    }


    private void createDefaultPreferences(
            Long userId
    ) {

        UserPreferenceDocument preferences =
                new UserPreferenceDocument();


        preferences.setUserId(
                userId
        );

        preferences.setDiet(
                ""
        );

        preferences.setPreferredCuisines(
                List.of()
        );

        preferences.setAllergies(
                List.of()
        );

        preferences.setDislikedIngredients(
                List.of()
        );

        preferences.setFavoriteIngredients(
                List.of()
        );

        preferences.setPreferredDifficulty(
                List.of()
        );

        preferences.setUpdatedAt(
                Instant.now()
        );


        preferenceRepository.save(
                preferences
        );
    }
}