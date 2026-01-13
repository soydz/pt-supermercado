package com.soydz.ptsupermercado.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import com.soydz.ptsupermercado.service.exception.StoreNotFoundException;
import com.soydz.ptsupermercado.service.interfaces.IStoreService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private IStoreService storeService;

  @Test
  void shouldReturn201WhenStoreIsValid() throws Exception {
    // Given
    StoreResDTO storeResDTO = new StoreResDTO(5L, "Arboleda", "Calle 11 # 15-25", "Cali");

    String reqJson =
        """
            {
              "name": "Arboleda",
              "address": "Calle 11 # 15-25",
              "city": "Cali"
            }
        """;

    // When
    when(storeService.save(any(StoreReqDTO.class))).thenReturn(storeResDTO);

    // Then
    mockMvc
        .perform(
            post("/api/v1/sucursales").contentType(MediaType.APPLICATION_JSON).content(reqJson))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/api/v1/sucursales/5"))
        .andExpect(jsonPath("$.name").value(storeResDTO.name()))
        .andExpect(jsonPath("$.address").value(storeResDTO.address()))
        .andExpect(jsonPath("$.city").value(storeResDTO.city()));
  }

  @Test
  void shouldReturn400WhenStoreIsInvalid() throws Exception {
    String reqJson =
        """
                  {
                    "name": "",
                    "address": "Calle 11 # 15-25",
                    "city": null
                  }
              """;

    mockMvc
        .perform(
            post("/api/v1/sucursales").contentType(MediaType.APPLICATION_JSON).content(reqJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors.length()").value(3))
        .andExpect(jsonPath("$.errors[*].field").value(containsInAnyOrder("city", "city", "name")));
  }

  @Test
  void shouldReturn200AndStoreListWhenStoresExist() throws Exception {
    // Given
    StoreResDTO storeResDTO = new StoreResDTO(13L, "Los colores", "calle 12 # 45-07", "Medellin");

    // When
    when(storeService.findAll()).thenReturn(List.of(storeResDTO));

    // Then
    mockMvc
        .perform(get("/api/v1/sucursales").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$.[0].id").value(storeResDTO.id()))
        .andExpect(jsonPath("$.[0].name").value(storeResDTO.name()))
        .andExpect(jsonPath("$.[0].address").value(storeResDTO.address()))
        .andExpect(jsonPath("$.[0].city").value(storeResDTO.city()));
  }

  @Test
  void shouldReturn200AndUpdateStoreWhenIdStoreExist() throws Exception {
    // Given
    StoreResDTO storeResDTO = new StoreResDTO(11L, "Primavera", "Calle 11 # 15-25", "Bucaramanga");

    String reqJson =
        """
            {
              "name": "Primavera",
              "address": "Calle 11 # 15-25",
              "city": "Bucaramanga"
            }
        """;

    // When
    when(storeService.update(any(StoreReqDTO.class), anyLong())).thenReturn(storeResDTO);

    // Then
    mockMvc
        .perform(
            put("/api/v1/sucursales/{id}", 11L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(storeResDTO.id()))
        .andExpect(jsonPath("$.name").value(storeResDTO.name()))
        .andExpect(jsonPath("$.address").value(storeResDTO.address()))
        .andExpect(jsonPath("$.city").value(storeResDTO.city()));

    verify(storeService).update(any(StoreReqDTO.class), eq(11L));
  }

  @Test
  void shouldReturn404WhenStoreDoesNotExist() throws Exception {
    // Given
    String reqJson =
        """
          {
            "name": "Primavera",
            "address": "Calle 11 # 15-25",
            "city": "Bucaramanga"
          }
        """;

    // When
    when(storeService.update(any(StoreReqDTO.class), anyLong()))
        .thenThrow(new StoreNotFoundException(11L));

    // Then
    mockMvc
        .perform(
            put("/api/v1/sucursales/{id}", 11L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn204WhenStoreIsDeleted() throws Exception {
    // Given
    Long storeId = 13L;

    // Then
    mockMvc.perform(delete("/api/v1/sucursales/{id}", storeId)).andExpect(status().isNoContent());

    verify(storeService).delete(storeId);
  }
}
