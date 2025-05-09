package no.ntnu.idatt2106.krisefikser.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import no.ntnu.idatt2106.krisefikser.model.User;

/**
 * Repository interface for managing User entities in the database.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and custom finder methods
 * for {@link User} entities.
 *
 * Used to retrieve users by username or email, check existence, and find users belonging to a household.
 *
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the {@link User} if found, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the {@link User} if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user with the given username exists.
     *
     * @param username the username to check for existence
     * @return true if a user with the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email address exists.
     *
     * @param email the email address to check for existence
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds all users associated with a specific household.
     *
     * @param householdId the ID of the household
     * @return a list of {@link User} entities belonging to the specified household
     */
    List<User> findByHousehold_Id(int householdId);

}
