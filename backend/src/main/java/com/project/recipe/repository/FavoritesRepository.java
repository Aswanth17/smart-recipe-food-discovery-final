package com.project.recipe.repository;

import com.project.recipe.model.FavoritesDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FavoritesRepository
        extends MongoRepository<FavoritesDocument, String> {

    Optional<FavoritesDocument> findByUserId(
            Long userId
    );
}