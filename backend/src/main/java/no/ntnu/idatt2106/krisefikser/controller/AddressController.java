package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.service.AddressService;

/**
 * Controller class for managing address-related operations in the system.
 * This class is responsible for handling HTTP requests related to addresses, such as creating, updating, deleting, and retrieving address information.
 * It interacts with the AddressService to perform the necessary operations and return appropriate responses.
 */
@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Address", description = "Operations related to addresses management")
public class AddressController {
  private final AddressService addressService;

  /**
   * Endpoint to retrieve all addresses in the system.
   * @return {@code ResponseEntity} containing a list of all addresses, or a 204 No Content status if no addresses are found.
   */
  @Operation(
    summary = "Get all addresses", 
    description = "Retrieve a list of all addresses in the system"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully"),
    @ApiResponse(responseCode = "204", description = "No addresses found")
  })
  @GetMapping
  public ResponseEntity<List<Address>> getAllAddresses() {
    List<Address> addresses = addressService.findAll();
    if (addresses.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(addresses);
    }
  }

  /**
   * Endpoint to retrieve an address by its ID.
   * @param id the ID of the address to retrieve.
   * @return {@code ResponseEntity} containing the address if found, or a 404 Not Found status if not found.
   */
  @Operation(
    summary = "Get address by ID", 
    description = "Retrieve an address based on its unique identifier"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Address not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<Address> getAddressById(
    @Parameter (description = "ID of the address to retrieve", example = "1", required = true)
    @PathVariable int id) {
    Address address = addressService.findById(id).orElse(null);
    if (address != null) {
      return ResponseEntity.ok(address);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Endpoint to create a new address.
   * @param address the address to create.
   * @return {@code ResponseEntity} containing the created address, or a 400 Bad Request status if the address is invalid.
   */
  @Operation(
    summary = "Create a new address", 
    description = "Create a new address in the system"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Address created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid address data, for example, missing required fields")
  })
  @PostMapping
  public ResponseEntity<AddressResponseDTO> createAddress(
    @Parameter (description = "Address object to be created", required = true)
    @RequestBody AddressRequestDTO address) {
    try {
      AddressResponseDTO createdAddress = addressService.save(address);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    } catch (Exception e) {
      return ResponseEntity.badRequest().header("Error message", e.getMessage()).build();
    }
  }

  /**
   * Endpoint to update an existing address.
   * @param id the ID of the address to update.
   * @param address the updated address information.
   * @return {@code ResponseEntity} containing the updated address, or a 404 Not Found status if not found.
   */
  @Operation(
    summary = "Update an existing address", 
    description = "Update an existing address in the system. " + 
                  "Returns a 404 Not Found status if the address does not exist, " +
                  "or a 400 Bad Request status if the address data is invalid."
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Address updated successfully"),
    @ApiResponse(responseCode = "404", description = "Address not found"),
    @ApiResponse(responseCode = "400", description = "Invalid address data, for example, missing required fields")
  })
  @PutMapping("/{id}")
  public ResponseEntity<Address> updateAddress(
    @Parameter (description = "ID of the address to update", example = "1", required = true)
    @PathVariable int id, 
    @Parameter (description = "Updated address object", required = true)
    @RequestBody Address address) {
    if (!addressService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    Address updatedAddress = addressService.updateAddress(id, address);
    return ResponseEntity.ok(updatedAddress);
  }

  /**
   * Endpoint to delete an address by its ID.
   * @param id the ID of the address to delete.
   * @return {@code ResponseEntity} indicating the result of the deletion operation.
   */
  @Operation(
    summary = "Delete an address", 
    description = "Delete an address based on its unique identifier"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Address not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAddress(
    @Parameter (description = "ID of the address to delete", example = "1", required = true)
    @PathVariable int id) {
    if (!addressService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    addressService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
  
}
