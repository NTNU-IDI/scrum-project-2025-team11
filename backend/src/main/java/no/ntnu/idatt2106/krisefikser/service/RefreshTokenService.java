package no.ntnu.idatt2106.krisefikser.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.PointOfInterestMapper;
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

    @Transactional
    public void revokeToken(String tokenString) {
        repository.findByToken(tokenString).ifPresent(token -> {
            token.setRevoked(true);
            repository.save(token);
        });
    }
}
