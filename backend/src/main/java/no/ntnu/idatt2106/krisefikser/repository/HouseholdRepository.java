package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.idatt2106.krisefikser.model.Household;


/**
 * Repository class for managing household data in the database. 
 * This class is responsible for performing CRUD operations on household entities.
 */
public interface HouseholdRepository extends JpaRepository<Household, Integer> {

    /**
     * Delete any Household that has no User pointing to it.
     * Returns the number of rows deleted.
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
      value = ""
        + "DELETE FROM household h "
        + "WHERE NOT EXISTS ("
        + "  SELECT 1 FROM user_account u "
        + "   WHERE u.household_id = h.id"
        + ")",
      nativeQuery = true
    )
    int deleteOrphanHouseholds();
}