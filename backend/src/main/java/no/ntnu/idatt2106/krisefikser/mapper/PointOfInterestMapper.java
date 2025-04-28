package no.ntnu.idatt2106.krisefikser.mapper;

import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;

/**
 * Mapper class to map household entities to PointOfInterestResponseDTO
 */
public class PointOfInterestMapper {

    public static PointOfInterestResponseDTO toResponseDTO(PointOfInterest pointOfInterest) {
        PointOfInterestResponseDTO pointOfInterestResponseDTO = new PointOfInterestResponseDTO();
        pointOfInterestResponseDTO.setId(pointOfInterest.getId());
        pointOfInterestResponseDTO.setName(pointOfInterest.getName());
        pointOfInterestResponseDTO.setIconType(pointOfInterest.getIconType());
        pointOfInterestResponseDTO.setDescription(pointOfInterest.getDescription());
        pointOfInterestResponseDTO.setLatitude(pointOfInterest.getLatitude());
        pointOfInterestResponseDTO.setLongitude(pointOfInterest.getLongitude());
        return pointOfInterestResponseDTO;
    }
}
