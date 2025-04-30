package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.Enums;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for point of interest entity.
 * This interface extends JpaRepository to provide CRUD operations for Point of interest.
 *
 * @see JpaRepository
 */
public interface PointOfInterestRepository extends JpaRepository<PointOfInterest, Integer> {
    List<PointOfInterest> findByIconType(Enums.IconEnum iconType);
}
