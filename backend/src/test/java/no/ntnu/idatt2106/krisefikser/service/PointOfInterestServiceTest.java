package no.ntnu.idatt2106.krisefikser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.PointOfInterestMapper;
import no.ntnu.idatt2106.krisefikser.model.Enums;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;
import no.ntnu.idatt2106.krisefikser.repository.PointOfInterestRepository;

public class PointOfInterestServiceTest {
    @Mock
    PointOfInterestRepository pointOfInterestRepository;

    @Mock
    PointOfInterestMapper pointOfInterestMapper;

    @InjectMocks
    private PointOfInterestService pointOfInterestService;

    private PointOfInterest pointOfInterest;
    private PointOfInterestRequestDTO pointOfInterestRequestDTO;
    private PointOfInterestResponseDTO pointOfInterestResponseDTO;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        pointOfInterest = new PointOfInterest();
        pointOfInterest.setId(0);
        pointOfInterest.setName("Test POI");
        pointOfInterest.setDescription("Test Description");
        pointOfInterest.setIconType(Enums.IconEnum.shelter);
        pointOfInterest.setLatitude(11.0);
        pointOfInterest.setLongitude(22.0);

        pointOfInterestRequestDTO = new PointOfInterestRequestDTO();
        pointOfInterestRequestDTO.setName(pointOfInterest.getName());
        pointOfInterestRequestDTO.setDescription(pointOfInterest.getDescription());
        pointOfInterestRequestDTO.setIconType(pointOfInterest.getIconType());
        pointOfInterestRequestDTO.setLatitude(pointOfInterest.getLatitude());
        pointOfInterestRequestDTO.setLongitude(pointOfInterest.getLongitude());

        pointOfInterestResponseDTO = new PointOfInterestResponseDTO();
        pointOfInterestResponseDTO.setId(pointOfInterest.getId());
        pointOfInterestResponseDTO.setName(pointOfInterest.getName());
        pointOfInterestResponseDTO.setDescription(pointOfInterest.getDescription());
        pointOfInterestResponseDTO.setIconType(pointOfInterest.getIconType());
        pointOfInterestResponseDTO.setLatitude(pointOfInterest.getLatitude());
        pointOfInterestResponseDTO.setLongitude(pointOfInterest.getLongitude());
    }

    @Nested
    @DisplayName("Positive test cases")
    class PositiveTestCases {
        @Test
        void testFindById() {
            when(pointOfInterestRepository.findById(1)).thenReturn(Optional.of(pointOfInterest));
            Optional<PointOfInterest> response = pointOfInterestService.findById(1);
            assertEquals(Optional.of(pointOfInterest), response);
        }

        @Test
        void testFindAll() {
            when(pointOfInterestRepository.findAll()).thenReturn(List.of(pointOfInterest));
            List<PointOfInterestResponseDTO> response = pointOfInterestService.findAll();
            assertEquals(1, response.size());
            assertEquals(pointOfInterestResponseDTO, response.get(0));
        }

        @Test
        void testSave() {
            when(pointOfInterestRepository.save(pointOfInterest)).thenReturn(pointOfInterest);
            PointOfInterestResponseDTO response = pointOfInterestService.save(pointOfInterestRequestDTO);
            assertEquals(pointOfInterestResponseDTO, response);
        }

        @Test
        void testFindByIconTypeList() {
            when(pointOfInterestRepository.findByIconTypeIn(List.of(Enums.IconEnum.shelter))).thenReturn(List.of(pointOfInterest));
            List<PointOfInterestResponseDTO> response = pointOfInterestService.findByIconType(List.of("shelter"));
            assertEquals(pointOfInterestResponseDTO, response.get(0));
            assertEquals(1, response.size());
        }

        @Test
        void testDeleteById() {
            pointOfInterestService.deleteById(1);
            when(pointOfInterestRepository.findById(1)).thenReturn(Optional.empty());
            Optional<PointOfInterest> response = pointOfInterestService.findById(1);
            assertEquals(Optional.empty(), response);
        }

    }

    @Nested
    @DisplayName("Negative test cases")
    class NegativeTestCases {
        @Test
        void testFindByIdNotFound() {
            when(pointOfInterestRepository.findById(99)).thenReturn(Optional.empty());
            Optional<PointOfInterest> response = pointOfInterestService.findById(99);
            assertEquals(Optional.empty(), response);
        }

        @Test
        void testSaveWithNullName() {
            pointOfInterestRequestDTO.setName(null);
            try {
                pointOfInterestService.save(pointOfInterestRequestDTO);
            } catch (IllegalArgumentException e) {
                assertEquals("Name cannot be null", e.getMessage());
            }
        }

        @Test
        void testSaveWithNullDescription() {
            pointOfInterestRequestDTO.setDescription(null);
            try {
                pointOfInterestService.save(pointOfInterestRequestDTO);
            } catch (IllegalArgumentException e) {
                assertEquals("Description cannot be null", e.getMessage());
            }
        }

        @Test
        void testSaveWithNullIconType() {
            pointOfInterestRequestDTO.setIconType(null);
            try {
                pointOfInterestService.save(pointOfInterestRequestDTO);
            } catch (IllegalArgumentException e) {
                assertEquals("IconType cannot be null", e.getMessage());
            }
        }

        @Test
        void testSaveWithNullLatitude() {
            pointOfInterestRequestDTO.setLatitude(null);
            try {
                pointOfInterestService.save(pointOfInterestRequestDTO);
            } catch (IllegalArgumentException e) {
                assertEquals("Latitude cannot be null", e.getMessage());
            }
        }

        @Test
        void testSaveWithNullLongitude() {
            pointOfInterestRequestDTO.setLongitude(null);
            try {
                pointOfInterestService.save(pointOfInterestRequestDTO);
            } catch (IllegalArgumentException e) {
                assertEquals("Longitude cannot be null", e.getMessage());
            }
        }
    }
}
