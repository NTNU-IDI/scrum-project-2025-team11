package no.ntnu.idatt2106.krisefikser.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import no.ntnu.idatt2106.krisefikser.model.RefreshToken;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken createRefreshToken(User user, String tokenString, long expiryMillis) {
        RefreshToken token = new RefreshToken();
        token.setToken(tokenString);
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpirationDate(LocalDateTime.now().plusDays(expiryMillis));
        token.setRevoked(false);

        return repository.save(token);
    }

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
