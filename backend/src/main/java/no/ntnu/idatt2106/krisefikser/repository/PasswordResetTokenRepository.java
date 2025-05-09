package no.ntnu.idatt2106.krisefikser.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import no.ntnu.idatt2106.krisefikser.model.PasswordResetToken;
import no.ntnu.idatt2106.krisefikser.model.User;

/**
 * Repository interface for managing PasswordResetToken entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations and custom finder methods for PasswordResetToken.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Retrieves a password reset token entity by its token string.
     *
     * @param token the unique token string to search for
     * @return an Optional containing the matching PasswordResetToken if found, or empty if not found
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Deletes all password reset token entities associated with the given user.
     *
     * @param user the user whose password reset tokens should be removed
     */
    void deleteByUser(User user);
}
