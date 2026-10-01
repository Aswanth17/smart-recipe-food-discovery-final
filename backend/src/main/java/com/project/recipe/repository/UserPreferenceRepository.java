package com.project.recipe.repository;

import com.project.recipe.model.UserPreferenceDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserPreferenceRepository
        extends MongoRepository<UserPreferenceDocument, String> {

    Optional<UserPreferenceDocument> findByUserId(
            Long userId
    );
}