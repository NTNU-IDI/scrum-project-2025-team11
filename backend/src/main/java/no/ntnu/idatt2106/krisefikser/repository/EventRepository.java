package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import no.ntnu.idatt2106.krisefikser.model.Event;

/**
 * Repository class for managing event data in the database.
 * It extends JpaRepository to provide CRUD operations on event entities and
 * JpaSpecificationExecutor for advanced querying capabilities.
 * This interface is used to interact with the database and perform operations on event records.
 * Additional custom query methods can be defined here if needed.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
  
}
