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
    List<HouseholdItem> findByHouseholdId(Integer householdId);
    List<HouseholdItem> findByHouseholdIdAndItemId(Integer householdId, Integer itemId);
}
