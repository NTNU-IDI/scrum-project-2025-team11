package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.ntnu.idatt2106.krisefikser.model.Address;

/**
 * Repository class for managing address data in the database. 
 * This class is responsible for performing CRUD operations on address entities.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
