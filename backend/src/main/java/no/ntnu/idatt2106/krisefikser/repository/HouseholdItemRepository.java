package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.HouseholdItem;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for managing household item data in the database.
 * This interface extends JpaRepository to provide CRUD operations on HouseholdItem entities.
 */
@Repository
public interface HouseholdItemRepository extends JpaRepository<HouseholdItem, HouseholdItemId> {
    /**
     * Finds all household items associated with a specific household ID.
     * @param householdId the ID of the household to search for
     * @return a list of HouseholdItem objects associated with the specified household ID
     */
    List<HouseholdItem> findByHouseholdId(Integer householdId);

    /**
     * Finds a specific household item by its household ID and item ID.
     * @param householdId the ID of the household
     * @param itemId the ID of the item
     * @return a list of HouseholdItem objects matching the specified household ID and item ID
     */
    List<HouseholdItem> findByHouseholdIdAndItemId(Integer householdId, Integer itemId);
}
