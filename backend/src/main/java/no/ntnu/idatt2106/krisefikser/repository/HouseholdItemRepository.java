package no.ntnu.idatt2106.krisefikser.repository;

import no.ntnu.idatt2106.krisefikser.model.HouseholdItem;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HouseholdItemRepository extends JpaRepository<HouseholdItem, HouseholdItemId> {
    List<HouseholdItem> findByHouseholdId(Integer householdId);
}
