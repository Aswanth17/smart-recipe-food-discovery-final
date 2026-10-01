package com.project.recipe.repository;

import com.project.recipe.model.InteractionDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InteractionRepository
        extends MongoRepository<InteractionDocument, String> {

    List<InteractionDocument> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );
}