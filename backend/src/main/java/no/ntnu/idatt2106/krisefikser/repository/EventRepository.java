package no.ntnu.idatt2106.krisefikser.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  /**
   * Find the largest radius of any event in the database.
   * Used to size the bounding box
   * @return the largest radius of any event in the database, or 0 if no events exist
   */
  @Query("SELECT MAX(e.radius) FROM Event e")
  Integer findMaxRadius();

  /**
   * Find all events whose center lies within the given latitude and longitude bounding box.
   * The query is safe against SQL injection attacks because it uses parameter binding.
   * @param lat the latitude of the center of the bounding box
   * @param lon the longitude of the center of the bounding box
   * @param degLat the half-width of the bounding box in degrees latitude
   * @param degLon the half-width of the bounding box in degrees longitude
   * @return a list of events whose center lies within the bounding box, or an empty list if none found
   */
  @Query("""
    SELECT e 
      FROM Event e
     WHERE e.latitude  BETWEEN :lat - :degLat AND :lat + :degLat
       AND e.longitude BETWEEN :lon - :degLon AND :lon + :degLon
    """)
  List<Event> findAllInBox(
    @Param("lat")    double lat,
    @Param("lon")    double lon,
    @Param("degLat") double degLat,
    @Param("degLon") double degLon
  );
}
