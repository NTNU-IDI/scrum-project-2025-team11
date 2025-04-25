package no.ntnu.idatt2106.krisefikser.mapper;

import no.ntnu.idatt2106.krisefikser.dto.ItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.ItemResponse;
import no.ntnu.idatt2106.krisefikser.model.Item;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Item entity and Item DTOs.
 */
@Component
public class ItemMapper {

    public ItemResponse toResponse(Item entity) {
        return new ItemResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription()
        );
    }

    public Item toEntity(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        return item;
    }
}
