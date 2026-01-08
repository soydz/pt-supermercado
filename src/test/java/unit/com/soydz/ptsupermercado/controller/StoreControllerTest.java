package com.soydz.ptsupermercado.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import com.soydz.ptsupermercado.service.interfaces.IStoreService;
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
}
