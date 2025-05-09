package no.ntnu.idatt2106.krisefikser.mapper;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItem;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItemId;
import no.ntnu.idatt2106.krisefikser.model.Item;
import org.springframework.stereotype.Component;

/**
 * Component for mapping between HouseholdItem entities and their DTOs.
 * <p>
 * Provides methods to construct a HouseholdItem entity from request data,
 * and to create a response DTO from a HouseholdItem entity.
 */
@Component
public class HouseholdItemMapper {

    /**
     * Converts a request DTO into a HouseholdItem entity.
     *
     * @param householdId the ID of the household to which the item belongs
     * @param req the incoming DTO containing item purchase details
     * @param household the managed Household entity
     * @param item the Item entity being added or linked
     * @return a new HouseholdItem entity with composite ID and provided details
     */
    public HouseholdItem toEntity(
        Integer householdId,
        HouseholdItemRequest req,
        Household household,
        Item item
    ) {
        HouseholdItemId id = new HouseholdItemId(
            householdId,
            req.getItemId(),
            req.getAcquiredDate()
        );
        return new HouseholdItem(
            id,
            household,
            item,
            req.getQuantity(),
            req.getUnit(),
            req.getExpirationDate()
        );
    }

    /**
     * Maps a HouseholdItem entity to its response DTO.
     *
     * @param hi the HouseholdItem entity to convert
     * @return a DTO containing household and item identifiers,
     *         quantities, units, and relevant dates
     */
    public HouseholdItemResponse toResponse(HouseholdItem hi) {
        return new HouseholdItemResponse(
            hi.getId().getHouseholdId(),
            hi.getId().getItemId(),
            hi.getQuantity(),
            hi.getUnit(),
            hi.getId().getAcquiredDate(),
            hi.getExpirationDate()
        );
    }
}
