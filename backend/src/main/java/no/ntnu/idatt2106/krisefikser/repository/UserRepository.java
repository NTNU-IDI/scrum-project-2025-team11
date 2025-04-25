package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import no.ntnu.idatt2106.krisefikser.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     * @param username
     * @return User object if found, otherwise null
     */
    User findByUsername(String username);

    /**
     * Finds a user by their email.
     * @param email
     * @return User object if found, otherwise null
     */
    User findByEmail(String email);

    /**
     * Find a user by their id
     * @param id
     * @return User object if found, otherwise null
     */
    User findById(int id);
    
    
}
