package com.soydz.ptsupermercado.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.soydz.ptsupermercado.dto.BestSellingProductResDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.service.exception.NoProductsSoldException;
import com.soydz.ptsupermercado.service.interfaces.IStatisticsService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean IStatisticsService statisticsService;

  @Test
  void shouldReturnBestSellingProductSuccessfully() throws Exception {
    // Given
    BestSellingProductResDTO dto =
        new BestSellingProductResDTO(
            new ProductResDTO(
                1L,
                "Leche entera alpina",
                "Lácteos",
                BigDecimal.valueOf(4500),
                "Lácteos la montaña"),
            5);

    // When
    when(statisticsService.bestSellingProduct()).thenReturn(List.of(dto));

    // Then
    mockMvc
        .perform(get("/api/v1/estadisticas/producto-mas-vendido"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$.[0].product.id").value(dto.product().id()))
        .andExpect(jsonPath("$.[0].product.name").value(dto.product().name()))
        .andExpect(jsonPath("$.[0].product.category").value(dto.product().category()))
        .andExpect(jsonPath("$.[0].product.supplierName").value(dto.product().supplierName()))
        .andExpect(jsonPath("$.[0].totalSold").value(dto.totalSold()));

    verify(statisticsService, times(1)).bestSellingProduct();
  }

  @Test
  void shouldReturnMultipleProductsWhenThereIsATie() throws Exception {
    // Given
    List<BestSellingProductResDTO> dtoList =
        List.of(
            new BestSellingProductResDTO(
                new ProductResDTO(
                    1L, "Pan", "Panadería", BigDecimal.valueOf(3500), "Granos del Tolima"),
                6),
            new BestSellingProductResDTO(
                new ProductResDTO(
                    2L, "Leche", "Lácteos", BigDecimal.valueOf(4200), "La Vaca Feliz"),
                6));

    // When
    when(statisticsService.bestSellingProduct()).thenReturn(dtoList);

    // Then
    mockMvc
        .perform(get("/api/v1/estadisticas/producto-mas-vendido"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void shouldReturn404WhenNoProductsSold() throws Exception {
    when(statisticsService.bestSellingProduct()).thenThrow(new NoProductsSoldException());

    mockMvc
        .perform(get("/api/v1/estadisticas/producto-mas-vendido"))
        .andExpect(status().isNotFound());
  }
}
