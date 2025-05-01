package no.ntnu.idatt2106.krisefikser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.idatt2106.krisefikser.model.RefreshToken;

/**
 * Repository interface for refresh token entity.
 * This interface extends JpaRepository to provide CRUD operations for refresh tokens.
 *
 * @see JpaRepository
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{
    Optional<RefreshToken> findByToken(String token);
}
