package no.ntnu.idatt2106.krisefikser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.HouseholdInviteCode;

/**
 * Repository interface for managing household invite codes in the database.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and custom finder methods
 * for {@link HouseholdInviteCode} entities.
 *
 * Used to retrieve invite codes by their code value and delete codes by household.
 *
 * @see JpaRepository
 */
public interface HouseholdInviteCodeRepository extends JpaRepository<HouseholdInviteCode, Integer> {

    /**
     * Finds a household invite code by its unique code string.
     *
     * @param code the invite code string to search for
     * @return an Optional containing the matching HouseholdInviteCode if found, or empty if not found
     */
    Optional<HouseholdInviteCode> findByCode(String code);

    /**
     * Deletes all invite code records associated with the given household.
     *
     * @param household the household whose invite codes should be removed
     */
    void deleteByHousehold(Household household);
}
