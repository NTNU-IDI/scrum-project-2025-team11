package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import no.ntnu.idatt2106.krisefikser.controller.InventoryController;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdItemResponse;
import no.ntnu.idatt2106.krisefikser.dto.UpsertInventoryRequest;
import no.ntnu.idatt2106.krisefikser.exceptionhandler.ResourceNotFoundException;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.security.JwtAuthFilter;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.InventoryService;
import no.ntnu.idatt2106.krisefikser.service.UserService;
import no.ntnu.idatt2106.krisefikser.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = InventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ TestSecurityConfig.class, GlobalExceptionHandler.class })
@ActiveProfiles("test")
class InventoryControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean InventoryService inventoryService;
    @MockitoBean UserService userService;
    @MockitoBean JwtUtil jwtUtil;
    @MockitoBean JwtAuthFilter jwtAuthFilter;

    // common setup: mock the authenticated user → household ID 42
    @BeforeEach
    void setupAuth() {
      when(userService.getUserByUsername(anyString()))
        .thenReturn(Optional.of(new User(/* … */, /* householdId= */42, /* … */)));
    }

    // 1) LIST all
    @Test @DisplayName("GET /api/households/items returns list")
    void listAll_success() throws Exception {
      List<HouseholdItemResponse> items = List.of(
        new HouseholdItemResponse(1, /*…*/),
        new HouseholdItemResponse(2, /*…*/)
      );
      when(inventoryService.list(42, null)).thenReturn(items);

      mvc.perform(get("/api/households/items"))
         .andExpect(status().isOk())
         .andExpect(content().json(mapper.writeValueAsString(items)));
    }

    // 2) LIST filtered
    @Test @DisplayName("GET /api/households/items?itemId=5 filters")
    void listFiltered_success() throws Exception {
      List<HouseholdItemResponse> items = List.of(/*…*/);
      when(inventoryService.list(42, 5)).thenReturn(items);

      mvc.perform(get("/api/households/items").param("itemId", "5"))
         .andExpect(status().isOk())
         .andExpect(content().json(mapper.writeValueAsString(items)));
    }

    // 3) ADD success
    @Test @DisplayName("POST /api/households/items adds item")
    void add_success() throws Exception {
      HouseholdItemRequest req = new HouseholdItemRequest(/*…*/);
      HouseholdItemResponse resp = new HouseholdItemResponse(5, /*…*/);
      when(inventoryService.add(42, req)).thenReturn(resp);

      mvc.perform(post("/api/households/items")
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(req)))
         .andExpect(status().isCreated())
         .andExpect(header().string("Location", "/api/households/42/items/5/" + resp.getAcquiredDate()))
         .andExpect(content().json(mapper.writeValueAsString(resp)));
    }

    // 4) ADD validation failure
    @Test @DisplayName("POST …/items returns 400 on invalid body")
    void add_validationFailure() throws Exception {
      HouseholdItemRequest invalid = new HouseholdItemRequest(/* missing required fields */);

      mvc.perform(post("/api/households/items")
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(invalid)))
         .andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    // 5) ADD not found (e.g. household or item missing)
    @Test @DisplayName("POST …/items returns 404 when service throws RNF")
    void add_notFound() throws Exception {
      HouseholdItemRequest req = new HouseholdItemRequest(/*…*/);
      doThrow(new ResourceNotFoundException("Item","id",99))
        .when(inventoryService).add(42, req);

      mvc.perform(post("/api/households/items")
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(req)))
         .andExpect(status().isNotFound());
    }

    // 6) UPDATE success
    @Test @DisplayName("PUT /api/households/items/{itemId}/{date} updates")
    void update_success() throws Exception {
      LocalDateTime when = LocalDateTime.now();
      HouseholdItemRequest req = new HouseholdItemRequest(/*…*/);
      req.setItemId(7);
      req.setAcquiredDate(when);
      HouseholdItemResponse resp = new HouseholdItemResponse(7, /*…*/);
      when(inventoryService.update(42, 7, req)).thenReturn(resp);

      mvc.perform(put("/api/households/items/7/{date}", when.toString())
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(req)))
         .andExpect(status().isOk())
         .andExpect(content().json(mapper.writeValueAsString(resp)));
    }

    // 7) UPDATE validation failure
    @Test @DisplayName("PUT … returns 400 on invalid body")
    void update_validationFailure() throws Exception {
      LocalDateTime when = LocalDateTime.now();
      HouseholdItemRequest invalid = new HouseholdItemRequest(); // blank

      mvc.perform(put("/api/households/items/7/{date}", when.toString())
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(invalid)))
         .andExpect(status().isBadRequest());
    }

    // 8) UPDATE not found
    @Test @DisplayName("PUT … returns 404 when service throws RNF")
    void update_notFound() throws Exception {
      LocalDateTime when = LocalDateTime.now();
      HouseholdItemRequest req = new HouseholdItemRequest(/*…*/);
      doThrow(new ResourceNotFoundException("Inventory","key", "…"))
        .when(inventoryService).update(42, 7, req);

      mvc.perform(put("/api/households/items/7/{date}", when.toString())
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(req)))
         .andExpect(status().isNotFound());
    }

    // 9) REMOVE ALL success
    @Test @DisplayName("DELETE /api/households/items/{itemId} removes all")
    void removeAll_success() throws Exception {
      doNothing().when(inventoryService).removeAll(42, 3);

      mvc.perform(delete("/api/households/items/3"))
         .andExpect(status().isNoContent());
    }

    // 10) REMOVE ALL not found
    @Test @DisplayName("DELETE … returns 404 when no purchases")
    void removeAll_notFound() throws Exception {
      doThrow(new ResourceNotFoundException("Inventory","itemId",3))
        .when(inventoryService).removeAll(42, 3);

      mvc.perform(delete("/api/households/items/3"))
         .andExpect(status().isNotFound());
    }

    // 11) REMOVE ONE success
    @Test @DisplayName("DELETE /api/households/items/{itemId}/{date} removes one")
    void removeOne_success() throws Exception {
      LocalDateTime when = LocalDateTime.now();
      doNothing().when(inventoryService).remove(42, 5, when);

      mvc.perform(delete("/api/households/items/5/{date}", when.toString()))
         .andExpect(status().isNoContent());
    }

    // 12) REMOVE ONE not found
    @Test @DisplayName("DELETE …/{date} returns 404 when not found")
    void removeOne_notFound() throws Exception {
      LocalDateTime when = LocalDateTime.now();
      doThrow(new ResourceNotFoundException("Inventory","key","…"))
        .when(inventoryService).remove(42, 5, when);

      mvc.perform(delete("/api/households/items/5/{date}", when.toString()))
         .andExpect(status().isNotFound());
    }

    // 13) UPSERT success
    @Test @DisplayName("POST /api/households/items/upsert creates or links")
    void upsert_success() throws Exception {
      UpsertInventoryRequest req = new UpsertInventoryRequest(/*…*/);
      HouseholdItemResponse resp = new HouseholdItemResponse(8, /*…*/);
      when(inventoryService.upsert(42, req)).thenReturn(resp);

      mvc.perform(post("/api/households/items/upsert")
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(req)))
         .andExpect(status().isCreated())
         .andExpect(content().json(mapper.writeValueAsString(resp)));
    }

    // 14) UPSERT validation failure
    @Test @DisplayName("POST …/upsert returns 400 on invalid body")
    void upsert_validationFailure() throws Exception {
      UpsertInventoryRequest invalid = new UpsertInventoryRequest(); // blank

      mvc.perform(post("/api/households/items/upsert")
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(invalid)))
         .andExpect(status().isBadRequest());
    }

    // 15) UPSERT not found
    @Test @DisplayName("POST …/upsert returns 404 when service throws RNF")
    void upsert_notFound() throws Exception {
      UpsertInventoryRequest req = new UpsertInventoryRequest(/*…*/);
      doThrow(new ResourceNotFoundException("Item","id",9))
        .when(inventoryService).upsert(42, req);

      mvc.perform(post("/api/households/items/upsert")
              .contentType(APPLICATION_JSON)
              .content(mapper.writeValueAsString(req)))
         .andExpect(status().isNotFound());
    }
}

