package com.soydz.ptsupermercado.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.soydz.ptsupermercado.dto.SaleReqDTO;
import com.soydz.ptsupermercado.dto.SaleResDTO;
import com.soydz.ptsupermercado.dto.SalesDetailsDTO;
import com.soydz.ptsupermercado.service.exception.StoreNotFoundException;
import com.soydz.ptsupermercado.service.interfaces.ISaleService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SaleController.class)
class SaleControllerTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean ISaleService saleService;

  @Test
  void shouldReturn201WhenSaleIsValid() throws Exception {
    // Given
    SaleResDTO saleResDTO =
        new SaleResDTO(
            7L,
            LocalDateTime.now(),
            2L,
            "primavera",
            List.of(new SalesDetailsDTO(2, "Arroz orquidea", BigDecimal.valueOf(3500))));

    String requestBody =
        """
      {
        "storeId": 7,
        "detail": [
          {
            "productId": 17,
            "quantity": 2
          }
        ]
      }
      """;

    // When
    when(saleService.save(any(SaleReqDTO.class))).thenReturn(saleResDTO);

    // Then
    mockMvc
        .perform(
            post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.id").value(saleResDTO.id()))
        .andExpect(jsonPath("$.storeId").value(saleResDTO.storeId()))
        .andExpect(jsonPath("$.storeName").value(saleResDTO.storeName()))
        .andExpect(jsonPath("$.salesDetails").isArray())
        .andExpect(jsonPath("$.salesDetails.length()").value(1))
        .andExpect(
            jsonPath("$.salesDetails[0].productName")
                .value(saleResDTO.salesDetails().getFirst().productName()))
        .andExpect(
            jsonPath("$.salesDetails[0].productPrice")
                .value(saleResDTO.salesDetails().getFirst().productPrice()))
        .andExpect(
            jsonPath("$.salesDetails[0].quantity")
                .value(saleResDTO.salesDetails().getFirst().quantity()));

    verify(saleService).save(any(SaleReqDTO.class));
  }

  @Test
  void shouldReturn400WhenSaleIsInvalid() throws Exception {
    // Given
    String invalidRequest =
        """
          {
            "storeId": null,
            "detail": [
              {
                "productId": 0,
                "quantity": 0
              }
            ]
          }
        """;

    // Then
    mockMvc
        .perform(
            post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON).content(invalidRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors[?(@.field=='storeId')]").exists())
        .andExpect(jsonPath("$.errors[?(@.field=='detail[0].productId')]").exists())
        .andExpect(jsonPath("$.errors[?(@.field=='detail[0].quantity')]").exists());

    verifyNoInteractions(saleService);
  }

  @Test
  void shouldReturn404WhenStoreNotFound() throws Exception {
    // Given
    Long storeId = 9L;

    String requestBody =
        """
      {
        "storeId": 9,
        "detail": [
          {
            "productId": 17,
            "quantity": 2
          }
        ]
      }
      """;

    // When
    when(saleService.save(any(SaleReqDTO.class))).thenThrow(new StoreNotFoundException(storeId));

    // Then
    mockMvc
        .perform(
            post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Store with id " + storeId + " not found"));

    verify(saleService).save(any(SaleReqDTO.class));
  }
}
