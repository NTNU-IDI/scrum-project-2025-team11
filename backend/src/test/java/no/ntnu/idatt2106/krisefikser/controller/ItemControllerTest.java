package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.dto.ItemRequest;
import no.ntnu.idatt2106.krisefikser.dto.ItemResponse;
import no.ntnu.idatt2106.krisefikser.mapper.ItemMapper;
import no.ntnu.idatt2106.krisefikser.model.Item;
import no.ntnu.idatt2106.krisefikser.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @MockBean private ItemService service;
    @MockBean private ItemMapper itemMapper;

    @Test
    @DisplayName("GET /api/items returns list of DTOs")
    void listItems() throws Exception {
        Item entity = new Item(1, "A", "a");
        ItemResponse resp = new ItemResponse(1, "A", "a");

        when(service.findAll()).thenReturn(List.of(entity));
        when(itemMapper.toResponse(entity)).thenReturn(resp);

        mvc.perform(get("/api/items"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$[0].id").value(1))
           .andExpect(jsonPath("$[0].name").value("A"));

        verify(service).findAll();
    }

    @Test
    @DisplayName("POST /api/items creates and returns DTO")
    void createItem() throws Exception {
        ItemRequest req = new ItemRequest();
        req.setName("B");
        req.setDescription("b");
        Item entity = new Item(null, "B", "b");
        Item saved = new Item(2, "B", "b");
        ItemResponse resp = new ItemResponse(2, "B", "b");

        when(itemMapper.toEntity(any())).thenReturn(entity);
        when(service.create(entity)).thenReturn(saved);
        when(itemMapper.toResponse(saved)).thenReturn(resp);

        mvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", "/api/items/2"))
           .andExpect(jsonPath("$.id").value(2))
           .andExpect(jsonPath("$.name").value("B"));

        verify(service).create(entity);
    }

    @Test
    @DisplayName("PUT /api/items/{id} updates when exists")
    void updateItem() throws Exception {
        ItemRequest req = new ItemRequest();
        req.setName("C");
        req.setDescription("c");
        Item toSave = new Item(3, "C", "c");
        Item updated = new Item(3, "C", "c");
        ItemResponse resp = new ItemResponse(3, "C", "c");

        when(service.findById(3)).thenReturn(Optional.of(toSave));
        when(itemMapper.toEntity(any(ItemRequest.class))).thenReturn(toSave);
        when(service.update(3, toSave)).thenReturn(updated);
        when(itemMapper.toResponse(updated)).thenReturn(resp);

        mvc.perform(put("/api/items/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(3))
           .andExpect(jsonPath("$.name").value("C"));
    }

    @Test
    @DisplayName("DELETE /api/items/{id} returns 204")
    void deleteItem() throws Exception {
        mvc.perform(delete("/api/items/5"))
           .andExpect(status().isNoContent());

        verify(service).delete(5);
    }
}
