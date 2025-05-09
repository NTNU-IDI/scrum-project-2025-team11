package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
* Repository interface for Item entity.
* This interface extends JpaRepository to provide CRUD operations for Item.
* 
* @see JpaRepository
*/
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    
    /**
    * Delete any Item whose id is not referenced by any HouseholdItem.
    * Returns the number of rows deleted.
    */
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
    value = "DELETE FROM ITEM WHERE NOT EXISTS (" +
    "  SELECT 1 FROM HOUSEHOLD_ITEMS hi WHERE hi.item_id = ITEM.id" +
    ")",
    nativeQuery = true
    )
    int deleteOrphanItems();
    
}