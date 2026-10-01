package com.project.recipe.config;

import com.project.recipe.security.JwtAuthenticationFilter;
import com.project.recipe.security.UserOwnershipFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserOwnershipFilter userOwnershipFilter;


    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserOwnershipFilter userOwnershipFilter
    ) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;

        this.userOwnershipFilter =
                userOwnershipFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .csrf(
                        csrf ->
                                csrf.disable()
                )

                .cors(
                        cors -> {
                        }
                )

                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )

                .authorizeHttpRequests(

                        auth -> auth

                                .requestMatchers(
                                        "/api/auth/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/recipes/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/restaurants/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/analytics/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/system/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/recommendations/**"
                                )
                                .authenticated()

                                .requestMatchers(
                                        "/api/users/**"
                                )
                                .authenticated()

                                .requestMatchers(
                                        "/api/profile/**"
                                )
                                .authenticated()

                                .anyRequest()
                                .permitAll()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .addFilterAfter(
                        userOwnershipFilter,
                        JwtAuthenticationFilter.class
                );


        return http.build();
    }
}