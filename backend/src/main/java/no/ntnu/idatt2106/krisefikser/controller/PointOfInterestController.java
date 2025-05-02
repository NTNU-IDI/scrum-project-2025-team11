package no.ntnu.idatt2106.krisefikser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.PointOfInterestResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.PointOfInterestMapper;
import no.ntnu.idatt2106.krisefikser.model.PointOfInterest;
import no.ntnu.idatt2106.krisefikser.service.PointOfInterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interest")
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
    @SecurityRequirement(name = "jwtCookieAuth")
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

    // Can be made to find points based on multiple iconTypes, discuss with frontend what they need
    @Operation(
            summary = "Find points",
            description = "Find multiple points based on the icon type given"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Points successfully found"),
            @ApiResponse(responseCode = "400", description = "Could not find points based on the given icontype," +
                    " is it a valid icontype?")
    })
    @GetMapping("/iconType")
    public ResponseEntity<List<PointOfInterestResponseDTO>> getPointsOfInterestsOnIconType(@RequestParam String iconType) {
        List<PointOfInterestResponseDTO> pointsOfInterest = pointOfInterestService.findByIconType(iconType);
        if (pointsOfInterest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pointsOfInterest);
    }

    @Operation(
            summary = "Create a point",
            description = "Create a point of interest by giving information about" +
                          "the name, description, what type of icon and position."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Point successfully created"),
            @ApiResponse(responseCode = "400", description = "Point could not be created," +
                    " make sure all fields are filled and that the icon type is valid")
    })
    @PostMapping
    public ResponseEntity<PointOfInterestResponseDTO> createPointOfInterest(@RequestBody PointOfInterestRequestDTO pointOfInterest) {
        PointOfInterestResponseDTO pointOfInterestResponse = pointOfInterestService.save(pointOfInterest);
        return ResponseEntity.ok(pointOfInterestResponse);
    }

    @Operation(
            summary = "Delete a point",
            description = "Delete a point based on a given Id "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Point successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Point could not be deleted, does this point even exist?")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePointOfInterest(@PathVariable int id) {
        if (!pointOfInterestService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pointOfInterestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Updates a point of interest",
        description = "Uses an Id to find and alter a tuple in the point of interest table"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Point successfully updated"),
        @ApiResponse(responseCode = "400", description = "Point could not be updated, check if the syntax is correct")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PointOfInterestResponseDTO> updatePointOfInterest(
        @PathVariable int id, @RequestBody PointOfInterestRequestDTO updatedPoint) {
        if (!pointOfInterestService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        PointOfInterestResponseDTO pointOfInterestResponse = pointOfInterestService.updatePointOfInterest(id, updatedPoint);
        return ResponseEntity.ok(pointOfInterestResponse);
    }
}
