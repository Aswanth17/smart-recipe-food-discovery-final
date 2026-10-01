package com.project.recipe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class UserOwnershipFilter
        extends OncePerRequestFilter {

    /*
     * Matches:
     *
     * /api/users/106/preferences
     * /api/users/106/favorites
     * /api/users/106/interactions
     *
     * Also:
     *
     * /api/recommendations/106
     */
    private static final Pattern USER_API_PATTERN =
            Pattern.compile(
                    "^/api/users/(\\d+)(?:/.*)?$"
            );


    private static final Pattern RECOMMENDATION_PATTERN =
            Pattern.compile(
                    "^/api/recommendations/(\\d+)(?:/.*)?$"
            );


    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    ) throws ServletException, IOException {


        String path =
                request.getRequestURI();


        Long requestedUserId =
                extractUserId(path);


        /*
         * Not a user-specific endpoint.
         * Continue normally.
         */
        if (requestedUserId == null) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        /*
         * If authentication has not happened,
         * Spring Security will deal with it later.
         */
        if (
                authentication == null
                ||
                !authentication.isAuthenticated()
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        /*
         * ADMIN users are allowed to access
         * another user's resources.
         */
        boolean isAdmin =
                authentication
                        .getAuthorities()
                        .stream()
                        .map(
                                GrantedAuthority::getAuthority
                        )
                        .anyMatch(
                                "ROLE_ADMIN"::equals
                        );


        if (isAdmin) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        Object principal =
                authentication
                        .getPrincipal();


        Long authenticatedUserId;


        try {

            if (principal instanceof Long) {

                authenticatedUserId =
                        (Long) principal;

            } else {

                authenticatedUserId =
                        Long.valueOf(
                                principal.toString()
                        );
            }

        } catch (Exception exception) {

            sendForbidden(
                    response
            );

            return;
        }


        /*
         * Important ownership check
         */
        if (
                !authenticatedUserId.equals(
                        requestedUserId
                )
        ) {

            sendForbidden(
                    response
            );

            return;
        }


        filterChain.doFilter(
                request,
                response
        );
    }


    private Long extractUserId(
            String path
    ) {

        Matcher userMatcher =
                USER_API_PATTERN
                        .matcher(path);


        if (userMatcher.matches()) {

            return Long.valueOf(
                    userMatcher.group(1)
            );
        }


        Matcher recommendationMatcher =
                RECOMMENDATION_PATTERN
                        .matcher(path);


        if (
                recommendationMatcher.matches()
        ) {

            return Long.valueOf(
                    recommendationMatcher.group(1)
            );
        }


        return null;
    }


    private void sendForbidden(
            HttpServletResponse response
    ) throws IOException {

        response.setStatus(
                HttpServletResponse.SC_FORBIDDEN
        );


        response.setContentType(
                "application/json"
        );


        response.getWriter()
                .write(
                        """
                        {
                          "status": 403,
                          "error": "Forbidden",
                          "message": "You cannot access another user's personal data."
                        }
                        """
                );
    }
}