package com.project.recipe.controller;

import com.project.recipe.dto.SystemHealthResponse;
import com.project.recipe.service.MongoHealthService;
import com.project.recipe.service.PostgresHealthService;
import com.project.recipe.xml.BaseXClientService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemHealthController {

    private final PostgresHealthService postgresHealthService;
    private final MongoHealthService mongoHealthService;
    private final BaseXClientService baseXClientService;

    public SystemHealthController(

            PostgresHealthService postgresHealthService,

            MongoHealthService mongoHealthService,

            BaseXClientService baseXClientService

    ) {

        this.postgresHealthService =
                postgresHealthService;

        this.mongoHealthService =
                mongoHealthService;

        this.baseXClientService =
                baseXClientService;
    }


    @GetMapping("/health")
    public SystemHealthResponse health() {

        boolean baseXConnected =
                baseXClientService.ping();


        return new SystemHealthResponse(

                "Smart Recipe Backend",

                postgresHealthService
                        .getDatabaseVersion(),

                postgresHealthService
                        .getPostgisVersion(),

                postgresHealthService
                        .getUserCount(),

                postgresHealthService
                        .getRestaurantCount(),

                mongoHealthService.ping()
                        ? "CONNECTED"
                        : "DISCONNECTED",

                mongoHealthService
                        .getPreferenceCount(),

                mongoHealthService
                        .getFavoriteCount(),

                mongoHealthService
                        .getInteractionCount(),

                baseXConnected
                        ? "CONNECTED"
                        : "DISCONNECTED",

                baseXConnected
                        ? baseXClientService
                        .getRecipeCount()
                        : 0
        );
    }
}