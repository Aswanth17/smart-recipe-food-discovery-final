package com.project.recipe.xml;

import com.project.recipe.config.BaseXProperties;

import org.basex.api.client.ClientQuery;
import org.basex.api.client.ClientSession;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class BaseXClientService {

    private final BaseXProperties properties;

    public BaseXClientService(
            BaseXProperties properties
    ) {
        this.properties = properties;
    }


    public String executeQuery(
            String xquery
    ) {

        return executeQuery(
                xquery,
                Map.of()
        );
    }


    public String executeQuery(
            String xquery,
            Map<String, String> variables
    ) {

        try (
                ClientSession session =
                        new ClientSession(
                                properties.getHost(),
                                properties.getPort(),
                                properties.getUsername(),
                                properties.getPassword()
                        );

                ClientQuery query =
                        session.query(xquery)
        ) {

            for (
                    Map.Entry<String, String> entry
                    : variables.entrySet()
            ) {

                query.bind(
                        entry.getKey(),
                        entry.getValue()
                );
            }

            return query.execute();

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Unable to communicate with BaseX server.",
                    exception
            );
        }
    }


    public String getDatabaseName() {
        return properties.getDatabase();
    }


    public boolean ping() {

        String query =
                "count(db:get('"
                        + escapeForXQuery(
                        properties.getDatabase()
                )
                        + "')/recipes/recipe)";

        try {

            String result =
                    executeQuery(query);

            Integer.parseInt(
                    result.trim()
            );

            return true;

        } catch (Exception exception) {

            return false;
        }
    }


    public int getRecipeCount() {

        String query =
                "count(db:get('"
                        + escapeForXQuery(
                        properties.getDatabase()
                )
                        + "')/recipes/recipe)";

        String result =
                executeQuery(query);

        return Integer.parseInt(
                result.trim()
        );
    }


    private String escapeForXQuery(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.replace(
                "'",
                "''"
        );
    }
}