package no.ntnu.idatt2106.krisefikser.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.AddressResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.AddressMapper;
import no.ntnu.idatt2106.krisefikser.model.Address;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.service.AddressService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

/**
* Controller class for managing address-related operations in the system.
* This class is responsible for handling HTTP requests related to addresses, such as creating, updating, deleting, and retrieving address information.
* It interacts with the AddressService to perform the necessary operations and return appropriate responses.
*/
@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "jwtCookieAuth")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Tag(name = "Address", description = "Operations related to addresses management")
public class AddressController {
  private final AddressService addressService;
  private final UserService userService;
  private final AddressMapper addressMapper;
  
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
  @PreAuthorize("hasRole('admin')")
  @GetMapping
  public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
    List<AddressResponseDTO> addresses = addressService.findAllAddresses();
    if (addresses.isEmpty()) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.ok(addresses);
    }
  }
  
  /**
  * Endpoint to retrieve the address of the logged-in user.
  * @return {@code ResponseEntity} containing the address if found, or a 404 Not Found status if not found.
  */
  @Operation(
  summary = "Get address of logged in user", 
  description = "Retrieve an address based on its unique identifier"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Address not found")
  })
  @GetMapping("/me")
  public ResponseEntity<AddressResponseDTO> getMyAddress() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    User user = userService.getUserByUsername(username).orElse(null);
    Household household = user.getHousehold();
    Address address = household.getAddress();
    AddressResponseDTO addressResponseDTO = addressMapper.toResponseDTO(address);
    if (addressResponseDTO != null) {
      return ResponseEntity.ok(addressResponseDTO);
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
  * Endpoint to update user's address.
  * @param address as a {@code AddressRequestDTO} object containing the updated address data.
  * @return {@code ResponseEntity} containing the updated address, or a 404 Not Found status if not found.
  */
  @Operation(
  summary = "Update a logged in users address", 
  description = "Update an existing address in the system. " + 
  "Returns a 404 Not Found status if the address does not exist, " +
  "or a 400 Bad Request status if the address data is invalid."
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Address updated successfully"),
    @ApiResponse(responseCode = "404", description = "Address not found"),
    @ApiResponse(responseCode = "400", description = "Invalid address data, for example, missing required fields")
  })
  @PutMapping
  public ResponseEntity<AddressResponseDTO> updateAddress(
  @Parameter (description = "Updated address object", required = true)
  @RequestBody AddressRequestDTO address) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();
      User user = userService.getUserByUsername(username).orElse(null);
      Household household = user.getHousehold();
      Address currentAddress = household.getAddress();
      AddressResponseDTO updatedAddress = addressService.updateAddress(currentAddress.getId(), address);
      return ResponseEntity.ok(updatedAddress);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().header("Error message", e.getMessage()).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().header("Error message", e.getMessage()).build();
    }
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
    try {
      addressService.deleteById(id);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().header("Error message", e.getMessage()).build();
    } catch (Exception e) {
      return ResponseEntity.notFound().header("Error message", e.getMessage()).build();
    }
    return ResponseEntity.noContent().build();
  }
  
}
