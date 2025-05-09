package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.Enums;
import no.ntnu.idatt2106.krisefikser.model.Enums.IconEnum;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing PointOfInterest entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and custom queries for points of interest.
 *
 * @see JpaRepository
 */
public interface PointOfInterestRepository extends JpaRepository<PointOfInterest, Integer> {

    /**
     * Finds all points of interest with the specified icon type.
     *
     * @param iconType the type of icon to filter points of interest by
     * @return a list of PointOfInterest entities matching the given icon type
     */
    List<PointOfInterest> findByIconType(Enums.IconEnum iconType);

    /**
     * Finds all points of interest whose icon type is in the specified list.
     *
     * @param iconTypes a list of icon types to filter points of interest by
     * @return a list of PointOfInterest entities matching any of the given icon types
     */
    List<PointOfInterest> findByIconTypeIn(List<IconEnum> iconTypes);
}