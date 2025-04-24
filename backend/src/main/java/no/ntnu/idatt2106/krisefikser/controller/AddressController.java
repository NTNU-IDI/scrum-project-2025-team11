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

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
  @GetMapping("/{id}")
  public ResponseEntity<Address> getAddressById(int id) {
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
  @PostMapping
  public ResponseEntity<Address> createAddress(@RequestBody Address address) {
    try {
      Address createdAddress = addressService.save(address);
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
  @PutMapping("/{id}")
  public ResponseEntity<Address> updateAddress(@PathVariable int id, @RequestBody Address address) {
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
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAddress(@PathVariable int id) {
    if (!addressService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    addressService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
  
  
}
