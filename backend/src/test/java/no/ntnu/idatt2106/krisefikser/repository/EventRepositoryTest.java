package no.ntnu.idatt2106.krisefikser.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import no.ntnu.idatt2106.krisefikser.model.Event;
import no.ntnu.idatt2106.krisefikser.model.Enums;

@ActiveProfiles("test")
@DataJpaTest
public class EventRepositoryTest {
  @Autowired
  private EventRepository eventRepository;

  Event event = new Event();
  @BeforeEach
  void setUp() {
    event.setName("Test Event");
    event.setDescription("This is a test event.");
    event.setStartTime(LocalDateTime.now());
    event.setEndTime(LocalDateTime.now().plusHours(2));
    event.setLatitude(10.0);
    event.setLongitude(20.0);
    event.setRadius(5);
    event.setIconType(Enums.IconEnum.shelter);
  }

  @Test
  void testCreateAndSaveEvent() {    
    Event savedEvent = eventRepository.save(event);
    
    var foundEvent = eventRepository.findById(savedEvent.getId());
    assertTrue(foundEvent.isPresent());
    assertEquals("Test Event", foundEvent.get().getName());
    assertEquals("This is a test event.", foundEvent.get().getDescription());
  }

  @Test
  void testFindEventById() {    
    Event savedEvent = eventRepository.save(event);
    
    var foundEvent = eventRepository.findById(savedEvent.getId());
    
    assertTrue(foundEvent.isPresent());
    assertEquals(savedEvent.getId(), foundEvent.get().getId());
  }

  @Test
  void testDeleteEvent() {
    Event savedEvent = eventRepository.save(event);
    
    eventRepository.delete(savedEvent);
    
    var foundEvent = eventRepository.findById(savedEvent.getId());
    
    assertTrue(foundEvent.isEmpty());
  }

  @Test
  void testFindAllEvents() {
    
    Event event2 = new Event();
    event2.setName("Event 2");
    event2.setDescription("Description 2");
    event2.setStartTime(LocalDateTime.now());
    event2.setEndTime(LocalDateTime.now().plusHours(3));
    event2.setLatitude(15.0);
    event2.setLongitude(25.0);
    event2.setRadius(10);
    event2.setIconType(Enums.IconEnum.shelter);

    
    eventRepository.save(event);
    eventRepository.save(event2);

    
    var allEvents = eventRepository.findAll();
    
    assertEquals(2, allEvents.size());
    assertEquals("Test Event", allEvents.get(0).getName());
    assertEquals("Event 2", allEvents.get(1).getName());
  }

  @Test
  void testfindByName() { 
    Event savedEvent = eventRepository.save(event);
    
    var foundEvent = eventRepository.findByName(savedEvent.getName());
    
    assertNotNull(foundEvent);
    assertEquals(savedEvent.getId(), foundEvent.getId());
  }

  @Test
  void testfindAllInBox() {
    Event event2 = new Event();
    event2.setName("Event 2");
    event2.setDescription("Description 2");
    event2.setStartTime(LocalDateTime.now());
    event2.setEndTime(LocalDateTime.now().plusHours(3));
    event2.setLatitude(15.0);
    event2.setLongitude(25.0);
    event2.setRadius(10);
    event2.setIconType(Enums.IconEnum.shelter);

    eventRepository.save(event);
    eventRepository.save(event2);

    double minLat = 5.0;
    double maxLat = 20.0;
    double minLon = 15.0;
    double maxLon = 30.0;

    var foundEvents = eventRepository.findAllInBox(minLat, maxLat, minLon, maxLon);

    assertEquals(2, foundEvents.size());
    assertEquals("Test Event", foundEvents.get(0).getName());
    assertEquals("Event 2", foundEvents.get(1).getName());
  }
}
