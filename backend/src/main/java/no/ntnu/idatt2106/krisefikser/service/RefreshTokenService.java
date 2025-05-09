package no.ntnu.idatt2106.krisefikser.service;

import jakarta.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import no.ntnu.idatt2106.krisefikser.model.RefreshToken;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.RefreshTokenRepository;

/**
 * Service class for creating, validating and deletion 
 * of refresh tokens
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    /**
     * Find all refresh tokens
     * @return All refresh tokens that exist in REFRESH TOKEN
     */
    public Optional<RefreshToken> findAll(int id) {
        return repository.findById(id);
    }

    /**
     * Creates a refresh token and saving it in the table connected to the user
     * @param user of type User represents the user that the token will be connected to
     * @param tokenString is a string which will be the token string
     * @param expiryMillis is how long the refresh token should stay available
     * @return returns a copy of the tuple generated in the REFRESH_TOKEN table
     */
    public RefreshToken createRefreshToken(User user, String tokenString, long expiryMillis) {
        RefreshToken token = new RefreshToken();
        token.setToken(tokenString);
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpirationDate(LocalDateTime.now().plusDays(expiryMillis));
        token.setRevoked(false);

        return repository.save(token);
    }

    /**
     * Checks if the refresh token is valid
     * @param tokenString of type string is the value of the refresh token
     *        we will check against
     * @return returns the information about the refresh token if it is valid
     */
    public RefreshToken validateRefreshToken(String tokenString) {
        RefreshToken token = repository.findByToken(tokenString)
            .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (token.getRevoked() || token.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        return token;
    }

    /**
     * Set refresh token to revoked making it not useable.
     * 
     * @param tokenString the string of the token to look up by
     */
    @Transactional
    public void revokeToken(String tokenString) {
        repository.findByToken(tokenString).ifPresent(token -> {
            token.setRevoked(true);
            repository.save(token);
        });
    }

    /**
     * Method to delete all revoked refresh tokens with expiration date before current time.
     * Should happen everyday at 2 AM.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanUpRevokedTokens() {
        repository.deleteAllByRevokedTrueAndExpirationDateBefore(Instant.now());
    }
}
