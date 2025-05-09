package no.ntnu.idatt2106.krisefikser.mapper;

import org.springframework.stereotype.Component;

import no.ntnu.idatt2106.krisefikser.dto.ItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.ItemResponse;
import no.ntnu.idatt2106.krisefikser.model.Item;

/**
 * Component responsible for converting between Item entities and their DTO representations.
 * <p>
 * Provides methods to map Item entities for API responses and to construct Item entities from request data.
 */
@Component
public class ItemMapper {

    /**
     * Converts an Item entity to its response DTO.
     *
     * @param entity the Item entity to convert
     * @return an ItemResponse DTO containing the item's id, name, and description
     */
    public ItemResponse toResponse(Item entity) {
        return new ItemResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription()
        );
    }

    /**
     * Constructs a new Item entity from the given request DTO.
     *
     * @param request the ItemRequest DTO containing name and description fields
     * @return a new Item entity populated with values from the request
     */
    public Item toEntity(ItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        return item;
    }
}
