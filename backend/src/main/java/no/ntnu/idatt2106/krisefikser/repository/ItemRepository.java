package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Item entity.
 * This interface extends JpaRepository to provide CRUD operations for Item.
 * 
 * @see JpaRepository
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

}