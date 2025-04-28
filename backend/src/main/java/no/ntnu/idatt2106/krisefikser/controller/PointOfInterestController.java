package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.PointOfInterestMapper;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;
import no.ntnu.idatt2106.krisefikser.service.PointOfInterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interest")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Point of interest", description = "Operations related to point of interest management")
public class PointOfInterestController {

    private final PointOfInterestService pointOfInterestService;

    @Operation(
            summary = "List all points of interest",
            description = "List all the point of interests that is in the point_of_interest table"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Points fetched"),
            @ApiResponse(responseCode = "400", description = "Points could not be found")
    })
    @GetMapping
    public ResponseEntity<List<PointOfInterestResponseDTO>> getPointsOfInterest() {
        List<PointOfInterestResponseDTO> pointOfInterestList = pointOfInterestService.findAll();
        if (pointOfInterestList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pointOfInterestList);
    }

    @Operation(
            summary = "A single point based on Id",
            description = "Find a single point based on the given id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Point fetched"),
            @ApiResponse(responseCode = "400", description = "Point could not be found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PointOfInterestResponseDTO> getPointOfInterestById(@PathVariable int id) {
        PointOfInterest pointOfInterest = pointOfInterestService.findById(id).orElse(null);
        if (pointOfInterest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PointOfInterestMapper.toResponseDTO(pointOfInterest));
    }

    @GetMapping("/iconType")
    public ResponseEntity<List<PointOfInterestResponseDTO>> getPointOfInterestsOnIconType(@RequestParam String iconType) {
        List<PointOfInterestResponseDTO> pointsOfInterest = pointOfInterestService.findByIconType(iconType);
        if (pointsOfInterest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pointsOfInterest);
    }
}
