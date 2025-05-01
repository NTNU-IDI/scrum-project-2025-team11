package no.ntnu.idatt2106.krisefikser.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import no.ntnu.idatt2106.krisefikser.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their username.
     * @param username
     * @return User object if found, otherwise null
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email.
     * @param email
     * @return User object if found, otherwise null
     */
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
