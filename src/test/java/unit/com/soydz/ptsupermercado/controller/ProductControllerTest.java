package com.soydz.ptsupermercado.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.service.impl.ProductServiceImpl;
import java.math.BigDecimal;
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
}
