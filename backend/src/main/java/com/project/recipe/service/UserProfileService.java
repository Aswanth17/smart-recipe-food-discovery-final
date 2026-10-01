package com.project.recipe.service;

import com.project.recipe.dto.UserResponse;

import com.project.recipe.repository.UserJdbcRepository;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserProfileService {

    private final UserJdbcRepository userRepository;


    public UserProfileService(
            UserJdbcRepository userRepository
    ) {

        this.userRepository =
                userRepository;
    }


    public UserResponse getProfile(
            Long userId
    ) {

        Map<String, Object> row =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "User not found."
                                        )
                        );


        return new UserResponse(

                ((Number)
                        row.get("user_id"))
                        .longValue(),

                row.get("full_name")
                        .toString(),

                row.get("email")
                        .toString(),

                row.get("role")
                        .toString(),

                Boolean.TRUE.equals(
                        row.get("is_active")
                )
        );
    }
}