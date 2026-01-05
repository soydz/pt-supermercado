package com.soydz.ptsupermercado.advice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.soydz.ptsupermercado.service.interfaces.IProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private IProductService productService;

  @Test
  void shouldReturn400WhenMethodArgumentNotValidExceptionOccurs() throws Exception {

    String body =
        """
        {
            "name":""
        }
        """;

    // Then
    mockMvc
        .perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation failed"))
        .andExpect(jsonPath("$.errors[?(@.field=='name')].message").value("must not be blank"))
        .andExpect(jsonPath("$.errors[?(@.field=='price')].message").value("must not be null"));
  }

  @Test
  void shouldReturn404WhenEntityNotFoundExceptionOccurs() throws Exception {
    // When
    when(productService.save(any())).thenThrow(new EntityNotFoundException());

    String body =
        """
        {
          "name": "Arroz",
          "category": "Grano",
          "price": 500,
          "supplierId": 4768464
        }
      """;

    // Then
    mockMvc
        .perform(post("/api/v1/productos").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isNotFound());
  }
}
