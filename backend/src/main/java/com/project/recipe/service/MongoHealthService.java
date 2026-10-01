package com.project.recipe.service;

import org.bson.Document;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoHealthService {

    private final MongoTemplate mongoTemplate;

    public MongoHealthService(
            MongoTemplate mongoTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean ping() {

        Document result =
                mongoTemplate
                        .getDb()
                        .runCommand(
                                new Document(
                                        "ping",
                                        1
                                )
                        );

        Object ok =
                result.get("ok");

        return ok != null
                && Double.parseDouble(
                        ok.toString()
                ) == 1.0;
    }

    public long getPreferenceCount() {

        return mongoTemplate
                .getCollection("preferences")
                .countDocuments();
    }

    public long getFavoriteCount() {

        return mongoTemplate
                .getCollection("favorites")
                .countDocuments();
    }

    public long getInteractionCount() {

        return mongoTemplate
                .getCollection("interactions")
                .countDocuments();
    }
}