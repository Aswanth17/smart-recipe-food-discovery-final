package com.project.recipe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    private final long expirationMs;


    public JwtService(

            @Value("${jwt.secret}")
            String secret,

            @Value("${jwt.expiration-ms}")
            long expirationMs

    ) {

        if (secret.length() < 32) {

            throw new IllegalStateException(
                    "JWT_SECRET must contain at least 32 characters."
            );
        }


        this.secretKey =
                Keys.hmacShaKeyFor(
                        secret.getBytes(
                                StandardCharsets.UTF_8
                        )
                );


        this.expirationMs =
                expirationMs;
    }


    public String generateToken(

            Long userId,

            String email,

            String role

    ) {

        Date now =
                new Date();


        Date expiration =
                new Date(
                        now.getTime()
                                + expirationMs
                );


        return Jwts
                .builder()
                .subject(
                        userId.toString()
                )
                .claim(
                        "email",
                        email
                )
                .claim(
                        "role",
                        role
                )
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }


    public Claims parseToken(
            String token
    ) {

        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public Long getUserId(
            String token
    ) {

        return Long.valueOf(
                parseToken(token)
                        .getSubject()
        );
    }


    public String getEmail(
            String token
    ) {

        return parseToken(token)
                .get(
                        "email",
                        String.class
                );
    }


    public String getRole(
            String token
    ) {

        return parseToken(token)
                .get(
                        "role",
                        String.class
                );
    }
}