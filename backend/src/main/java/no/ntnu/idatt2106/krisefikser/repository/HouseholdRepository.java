package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.idatt2106.krisefikser.model.Household;


/**
 * Repository class for managing household data in the database. 
 * This class is responsible for performing CRUD operations on household entities.
 */
public interface HouseholdRepository extends JpaRepository<Household, Integer> {

}
