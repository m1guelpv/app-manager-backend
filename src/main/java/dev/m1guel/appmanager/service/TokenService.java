package dev.m1guel.appmanager.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.m1guel.appmanager.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class TokenService {

    private static final String ISSUER = "auth-api";
    private static final long EXPIRATION_HOURS = 2;

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        Algorithm algorithm = getAlgorithm();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(user.getEmail())
                .withExpiresAt(generateExpirationDate())
                .sign(algorithm);
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = getAlgorithm();
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Algorithm getAlgorithm() {
        return Optional.ofNullable(secret)
                .filter(s -> !s.isBlank())
                .map(Algorithm::HMAC256)
                .orElseThrow(() -> new IllegalArgumentException("JWT secret cannot be null or blank"));
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(EXPIRATION_HOURS)
                .toInstant(ZoneOffset.UTC);
    }

}