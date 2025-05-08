package no.ntnu.idatt2106.krisefikser.service;

import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.PointOfInterestMapper;
import no.ntnu.idatt2106.krisefikser.model.Enums;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;
import no.ntnu.idatt2106.krisefikser.repository.PointOfInterestRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointOfInterestService {
    private final PointOfInterestRepository pointOfInterestRepository;

    /**
     * Find a single point based on an id
     * @param id Unique identifier
     * @return A single tuple in the point_of_interest table
     */
    public Optional<PointOfInterest> findById(int id) {
        return pointOfInterestRepository.findById(id);
    }

    /**
     * Find all points of interest
     * @return All points of interest that exist in the table
     */
    public List<PointOfInterestResponseDTO> findAll() {
        return pointOfInterestRepository.findAll()
                .stream()
                .map(PointOfInterestMapper::toResponseDTO)
                .toList();
    }

    /**
     * Save a new point of interest to the table
     * @param newPointOfInterest of the type PointOfInterestRequestDTO which contains all
     *                           the information about a point of interest
     * @return PointOfInterestResponseDTO which is all the information given about that point
     * plus the auto generated unique identifier
     */
    public PointOfInterestResponseDTO save(PointOfInterestRequestDTO newPointOfInterest) {
        PointOfInterest pointOfInterest = new PointOfInterest();

        if (newPointOfInterest.getName() == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (newPointOfInterest.getDescription() == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (newPointOfInterest.getIconType() == null) {
            throw new IllegalArgumentException("IconType cannot be null");
        }
        if (newPointOfInterest.getLatitude() == null) {
            throw new IllegalArgumentException("Latitude cannot be null");
        }
        if (newPointOfInterest.getLongitude() == null) {
            throw new IllegalArgumentException("Longitude cannot be null");
        }

        pointOfInterest.setName(newPointOfInterest.getName());
        pointOfInterest.setDescription(newPointOfInterest.getDescription());
        pointOfInterest.setIconType(newPointOfInterest.getIconType());
        pointOfInterest.setLatitude(newPointOfInterest.getLatitude());
        pointOfInterest.setLongitude(newPointOfInterest.getLongitude());

        pointOfInterestRepository.save(pointOfInterest);
        return PointOfInterestMapper.toResponseDTO(pointOfInterest);
    }

    /**
     * Find multiple points of interest based on the RequestParam
     * @param iconTypeString String that will be used to sort after points of interest
     * @return A list of points that match the input parameter
     */
    public List<PointOfInterestResponseDTO> findByIconType(List<String> iconTypeString) {
        List<Enums.IconEnum> iconTypes = iconTypeString.stream()
        .map(String::toLowerCase)
        .map(type -> {
            try {
                return Enums.IconEnum.valueOf(type);
            }   catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid icon type: " + type);
            }
        })
        .toList();

        return pointOfInterestRepository.findByIconTypeIn(iconTypes)
                .stream()
                .map(PointOfInterestMapper::toResponseDTO)
                .toList();
    }

    /**
     * Updates an already existing point of interest
     * @param id Unique identifier used to make a lookup in the table point of interest
     * @param requestDto A format for how data should be sent when making the api call
     * @return returns a PointOfInterestResponseDTO which has the updated and non updated fields
     * of the changed entry.
     */
    public PointOfInterestResponseDTO updatePointOfInterest(int id, PointOfInterestRequestDTO requestDto) {
        PointOfInterest currentPointOfInterest = pointOfInterestRepository.findById(id).orElseThrow(() -> new RuntimeException("Point of interest id not found"));
        if (requestDto.getName() != null) {
            currentPointOfInterest.setName(requestDto.getName());
        }
        if (requestDto.getIconType() != null) {
            currentPointOfInterest.setIconType(requestDto.getIconType());
        }
        if (requestDto.getDescription() != null) {
            currentPointOfInterest.setDescription(requestDto.getDescription());
        }
        if (requestDto.getLatitude() != null) {
            currentPointOfInterest.setLatitude(requestDto.getLatitude());
        }
        if (requestDto.getLongitude() != null) {
            currentPointOfInterest.setLongitude(requestDto.getLongitude());
        }
    
        PointOfInterest updatedPointOfInterest = pointOfInterestRepository.save(currentPointOfInterest);
        return PointOfInterestMapper.toResponseDTO(updatedPointOfInterest);
    }

    /**
     * Checks if a point exists by its ID.
     *
     * @param id The ID of the point to check.
     * @return True if the point exists, false otherwise.
     */
    public boolean existsById(int id) {
        return pointOfInterestRepository.existsById(id);
    }

    /**
     * Delete a point entry in the point of interest
     * table based on the given id
     * @param id The id of the point we wish to delete.
     */
    public void deleteById(int id) {
        pointOfInterestRepository.deleteById(id);
    }


    /**
     * Find the three closest shelters to a given point
     * @param latitude The latitude of the point
     * @param longitude The longitude of the point
     * @return A list of the three closest shelters
     */
    public List<PointOfInterestResponseDTO> findThreeClosestShelters(double latitude, double longitude) {
        List<PointOfInterest> shelters = pointOfInterestRepository.findByIconType(Enums.IconEnum.shelter);
        return shelters.stream()
        .sorted(Comparator.comparingDouble(
            p -> haversineKm(p.getLatitude(), p.getLongitude(), latitude, longitude)))
        .limit(3)
        .map(PointOfInterestMapper::toResponseDTO)
        .toList();
    }

    private static double haversineKm(double lat1, double lon1,
                                  double lat2, double lon2) {
        final double R = 6371.0;                // Earth radius km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
