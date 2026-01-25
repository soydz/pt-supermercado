package com.soydz.ptsupermercado.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.soydz.ptsupermercado.dto.SaleReqDTO;
import com.soydz.ptsupermercado.dto.SaleResDTO;
import com.soydz.ptsupermercado.dto.SalesDetailsDTO;
import com.soydz.ptsupermercado.service.exception.StoreNotFoundException;
import com.soydz.ptsupermercado.service.interfaces.ISaleService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  @Test
  void shouldReturn200WhenSuccessfullyRetrievedSalesList() throws Exception {
    // Given
    Long storeId = 1L;
    LocalDate creationDate = LocalDate.of(2026, 1, 13);

    SalesDetailsDTO salesDetailsDTO =
        new SalesDetailsDTO(4, "Avena del molino", BigDecimal.valueOf(3550));

    SaleResDTO saleResDTO =
        new SaleResDTO(3L, creationDate.atStartOfDay(), 2L, "Las Torres", List.of(salesDetailsDTO));

    // When
    when(saleService.findByStoreIdAndCreationDate(storeId, creationDate))
        .thenReturn(List.of(saleResDTO));

    // Then
    mockMvc
        .perform(
            get("/api/v1/ventas")
                .param("sucursalId", "1")
                .param("fecha", "2026-01-13")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$.[0].id").value(saleResDTO.id()))
        .andExpect(jsonPath("$.[0].storeName").value(saleResDTO.storeName()))
        .andExpect(jsonPath("$.[0].storeId").value(saleResDTO.storeId()))
        .andExpect(
            jsonPath("$.[0].creationDate")
                .value(saleResDTO.creationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .andExpect(
            jsonPath("$.[0].salesDetails.[0].productName")
                .value(saleResDTO.salesDetails().getFirst().productName()))
        .andExpect(
            jsonPath("$.[0].salesDetails.[0].productPrice")
                .value(saleResDTO.salesDetails().getFirst().productPrice()))
        .andExpect(
            jsonPath("$.[0].salesDetails.[0].quantity")
                .value(saleResDTO.salesDetails().getFirst().quantity()));
  }

  @Test
  void shouldReturn400WhenStoreIdParamIsMissing() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/ventas").param("fecha", "2026-01-12").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400WhenDateFormatIsInvalid() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/ventas")
                .param("sucursalId", "2")
                .param("fecha", "12-01-2026")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn404WhenStoreDoesNotExist() throws Exception {
    when(saleService.findByStoreIdAndCreationDate(anyLong(), any(LocalDate.class)))
        .thenThrow(StoreNotFoundException.class);

    // Then
    mockMvc
        .perform(
            get("/api/v1/ventas")
                .param("sucursalId", "2")
                .param("fecha", "2026-01-12")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturn200WithEmptyListWhenNoSalesFound() throws Exception {
    when(saleService.findByStoreIdAndCreationDate(anyLong(), any(LocalDate.class)))
        .thenReturn(List.of());

    // Then
    mockMvc
        .perform(
            get("/api/v1/ventas")
                .param("sucursalId", "1")
                .param("fecha", "2026-01-12")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void shouldReturn204WhenSaleIsDeleted() throws Exception {
    // Given
    Long saleId = 1L;

    // Then
    mockMvc.perform(delete("/api/v1/ventas/{id}", saleId)).andExpect(status().isNoContent());

    verify(saleService).delete(saleId);
  }
}
