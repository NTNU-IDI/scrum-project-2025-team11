package no.ntnu.idatt2106.krisefikser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import no.ntnu.idatt2106.krisefikser.model.TwoFactorCode;
import no.ntnu.idatt2106.krisefikser.model.User;

/**
 * Repository interface for managing two-factor authentication codes in the database.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and custom finder methods
 * for {@link TwoFactorCode} entities.
 *
 * Used to retrieve codes by their string value and remove codes by user.
 *
 * @see JpaRepository
 */
@Repository
public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Integer> {

    /**
     * Finds a two-factor authentication code entity by its code string.
     *
     * @param code the unique two-factor code string to search for
     * @return an Optional containing the matching {@link TwoFactorCode} if found, or empty if not found
     */
    Optional<TwoFactorCode> findByCode(String code);

    /**
     * Deletes all two-factor authentication code records associated with the given user.
     *
     * @param user the user whose two-factor codes should be removed
     */
    void deleteByUser(User user);
}
