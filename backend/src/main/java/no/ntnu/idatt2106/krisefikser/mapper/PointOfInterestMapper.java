package no.ntnu.idatt2106.krisefikser.mapper;

import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;

/**
 * Utility class for converting PointOfInterest entities into their response DTO format.
 * <p>
 * Provides a method to transform the domain model into a transport-friendly structure
 * suitable for API responses.
 */
public class PointOfInterestMapper {

    /**
     * Maps a PointOfInterest entity to a PointOfInterestResponseDTO.
     *
     * @param pointOfInterest the source entity containing POI details
     * @return a populated PointOfInterestResponseDTO with id, name, icon type,
     *         description, and geographic coordinates
     */
    public static PointOfInterestResponseDTO toResponseDTO(PointOfInterest pointOfInterest) {
        PointOfInterestResponseDTO dto = new PointOfInterestResponseDTO();
        dto.setId(pointOfInterest.getId());
        dto.setName(pointOfInterest.getName());
        dto.setIconType(pointOfInterest.getIconType());
        dto.setDescription(pointOfInterest.getDescription());
        dto.setLatitude(pointOfInterest.getLatitude());
        dto.setLongitude(pointOfInterest.getLongitude());
        return dto;
    }
}