package no.ntnu.idatt2106.krisefikser.mapper;

import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItem;
import no.ntnu.idatt2106.krisefikser.model.HouseholdItemId;
import no.ntnu.idatt2106.krisefikser.model.Item;
import org.springframework.stereotype.Component;

@Component
public class HouseholdItemMapper {

    public HouseholdItem toEntity(Integer householdId,
                                  HouseholdItemRequest req,
                                  Household household,
                                  Item item) {
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