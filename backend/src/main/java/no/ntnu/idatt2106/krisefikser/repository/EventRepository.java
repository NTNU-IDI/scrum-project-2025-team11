package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import no.ntnu.idatt2106.krisefikser.dto.EventResponseDTO;
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
  /**
   * Custom query method to find an event by its name.
   * @param name the name of the event to search for
   * @return the event with the specified name, or null if not found
   */
  Event findByName(String name);

  /**
   * Finds events by icon type and sorts them by start time in descending order.
   * @param iconType the icon type to filter events by
   * @return a list of events matching the specified icon type, sorted by start time in descending order, or an empty list if none found
   */
  List<EventResponseDTO> findByIconTypeOrderByStartTimeDesc(String iconType);
}
