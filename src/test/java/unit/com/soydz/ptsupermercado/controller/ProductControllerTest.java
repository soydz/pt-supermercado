package com.soydz.ptsupermercado.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.service.exception.ProductNotFoundException;
import com.soydz.ptsupermercado.service.impl.ProductServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ProductServiceImpl productService;

  @Test
  void shouldReturn400WhenProductIsNull() throws Exception {
    // Given
    ProductResDTO res = new ProductResDTO(1L, "", "Granos", BigDecimal.ZERO, "Granos del sol");

    // When
    when(productService.save(any())).thenReturn(res);

    // Then
    mockMvc
        .perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn201WhenProductIsValid() throws Exception {
    // Given
    ProductResDTO res =
        new ProductResDTO(1L, "Arroz floral", "Granos", BigDecimal.TEN, "Granos del sol");

    String resJson =
        """
        {
          "id": 9,
          "name": "Arroz floral",
          "category": "Granos",
          "price": 10.0,
          "supplierName": "Granos del sol"
        }
        """;

    // When
    when(productService.save(any())).thenReturn(res);

    // Then
    mockMvc
        .perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON).content(resJson))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "http://localhost/api/v1/productos/1"))
        .andExpect(jsonPath("$.name").value("Arroz floral"));
  }

  @Test
  void shouldReturn200AndProductListWhenProductsExist() throws Exception {
    // Given
    ProductResDTO productResDTO =
        new ProductResDTO(1L, "Arroz floral", "Granos", BigDecimal.TEN, "Granos del sol");

    // When
    when(productService.findAll()).thenReturn(List.of(productResDTO));

    // Then
    mockMvc
        .perform(get("/api/v1/productos").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].id").value(productResDTO.id()))
        .andExpect(jsonPath("$[0].name").value(productResDTO.name()))
        .andExpect(jsonPath("$[0].category").value(productResDTO.category()))
        .andExpect(jsonPath("$[0].price").value(productResDTO.price()))
        .andExpect(jsonPath("$[0].supplierName").value(productResDTO.supplierName()));
  }

  @Test
  void shouldReturn200AndUpdateProductWhenIdProductExist() throws Exception {
    // Given
    ProductResDTO productResDTO =
        new ProductResDTO(9L, "Arroz floral", "Granos", BigDecimal.TEN, "Granos del sol");

    String reqJson =
        """
              {
                "id": 9,
                "name": "Arroz floral",
                "category": "Granos",
                "price": 10.0,
                "supplierName": "Granos del sol"
              }
              """;

    // When
    when(productService.update(any(ProductReqDTO.class), eq(9L))).thenReturn(productResDTO);

    // Then
    mockMvc
        .perform(
            put("/api/v1/productos/{id}", 9L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(productResDTO.id()))
        .andExpect(jsonPath("$.name").value(productResDTO.name()))
        .andExpect(jsonPath("$.category").value(productResDTO.category()))
        .andExpect(jsonPath("$.price").value(productResDTO.price()))
        .andExpect(jsonPath("$.supplierName").value(productResDTO.supplierName()));

    verify(productService).update(any(ProductReqDTO.class), eq(9L));
  }

  @Test
  void shouldReturn404WhenProductDoesNotExist() throws Exception {
    // Given
    String reqJson =
        """
            {
              "id": 9,
              "name": "Arroz floral",
              "category": "Granos",
              "price": 10.0,
              "supplierName": "Granos del sol"
            }
        """;

    // When
    when(productService.update(any(ProductReqDTO.class), eq(9L)))
        .thenThrow(new ProductNotFoundException(9L));

    // Then
    mockMvc
        .perform(
            put("/api/v1/productos/{id}", 9L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
        .andExpect(status().isNotFound());
  }
}
