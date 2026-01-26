package com.soydz.ptsupermercado.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.soydz.ptsupermercado.dto.BestSellingProductResDTO;
import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.Sale;
import com.soydz.ptsupermercado.entity.SalesDetails;
import com.soydz.ptsupermercado.entity.Supplier;
import com.soydz.ptsupermercado.repository.ISalesDetailsRepository;
import com.soydz.ptsupermercado.service.exception.NoProductsSoldException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

  @Mock private ISalesDetailsRepository salesDetailsRepository;

  @InjectMocks private StatisticsServiceImpl statisticsService;

  private Product product1;
  private Product product2;
  private Product product3;
  private Sale sale1;
  private Sale sale2;

  @BeforeEach
  void setup() {
    sale1 = new Sale();
    sale1.setId(1L);
    sale2 = new Sale();
    sale2.setId(2L);

    Supplier supplier1 = new Supplier("Granos del Tolima", "clientes@granostolima.com");
    Supplier supplier2 = new Supplier("Lácteos La Vaca Feliz", "clientes@lacteosvacafeliz.com");

    product1 = new Product();
    product1.setId(1L);
    product1.setName("Pan Bimbo");
    product1.setCategory("Panadería");
    product1.setPrice(BigDecimal.valueOf(3500));
    product1.setSupplier(supplier1);
    product1.setSalesDetails(new ArrayList<>());

    product2 = new Product();
    product2.setId(2L);
    product2.setName("Leche Entera Alpina");
    product2.setCategory("Lácteos");
    product2.setPrice(BigDecimal.valueOf(4200));
    product2.setSupplier(supplier2);
    product2.setSalesDetails(new ArrayList<>());

    product3 = new Product();
    product3.setId(3L);
    product3.setName("Huevos AA x12");
    product3.setCategory("Huevos");
    product3.setPrice(BigDecimal.valueOf(9800));
    product3.setSupplier(supplier1);
    product3.setSalesDetails(new ArrayList<>());
  }

  @Test
  void shouldReturnProductListWhenBestSellingProductIsSuccessfully() {
    // Given
    List<SalesDetails> salesDetailsList =
        List.of(
            new SalesDetails(sale1, product1, 2),
            new SalesDetails(sale1, product2, 1),
            new SalesDetails(sale1, product3, 4),
            new SalesDetails(sale2, product1, 3),
            new SalesDetails(sale2, product2, 5),
            new SalesDetails(sale2, product3, 1));

    Product bestProduct = product2;

    // When
    when(salesDetailsRepository.findAll()).thenReturn(salesDetailsList);

    List<BestSellingProductResDTO> result = statisticsService.bestSellingProduct();

    // Then
    assertNotNull(result);
    assertEquals(bestProduct.getId(), result.getFirst().product().id());
    assertEquals(bestProduct.getName(), result.getFirst().product().name());
    assertEquals(bestProduct.getSupplier().getName(), result.getFirst().product().supplierName());
    assertEquals(bestProduct.getCategory(), result.getFirst().product().category());
    assertEquals(bestProduct.getPrice(), result.getFirst().product().price());
    assertEquals(6, result.getFirst().totalSold());

    verify(salesDetailsRepository, times(1)).findAll();
  }

  @Test
  void shouldThrowNoProductSoldExceptionWhenSalesIsEmpty() {
    // Given
    List<SalesDetails> salesDetailsList = Collections.emptyList();

    // When
    when(salesDetailsRepository.findAll()).thenReturn(salesDetailsList);

    // then
    assertThrows(NoProductsSoldException.class, () -> statisticsService.bestSellingProduct());

    verify(salesDetailsRepository).findAll();
  }

  @Test
  void shouldReturnProductWhenBestSellingProductIsSuccessfully2() {
    // Given
    List<SalesDetails> salesDetailsList =
        List.of(
            new SalesDetails(sale1, product1, 3),
            new SalesDetails(sale1, product2, 1),
            new SalesDetails(sale1, product3, 4),
            new SalesDetails(sale2, product1, 3),
            new SalesDetails(sale2, product2, 5),
            new SalesDetails(sale2, product3, 2));

    List<Product> bestProduct = List.of(product1, product2, product3);
    Integer maxSale = 6;

    // When
    when(salesDetailsRepository.findAll()).thenReturn(salesDetailsList);

    List<BestSellingProductResDTO> result = statisticsService.bestSellingProduct();

    // Then
    assertNotNull(result);
    assertEquals(bestProduct.size(), result.size());
    assertTrue(
        result.stream()
            .anyMatch(
                dto ->
                    dto.product().id().equals(product1.getId())
                        && dto.product().name().equals(product1.getName())
                        && dto.product().price().equals(product1.getPrice())
                        && dto.product().category().equals(product1.getCategory())
                        && dto.product().supplierName().equals(product1.getSupplier().getName())
                        && maxSale.equals(dto.totalSold())));

    assertTrue(
        result.stream()
            .anyMatch(
                dto ->
                    dto.product().id().equals(product2.getId())
                        && dto.product().name().equals(product2.getName())
                        && dto.product().price().equals(product2.getPrice())
                        && dto.product().category().equals(product2.getCategory())
                        && dto.product().supplierName().equals(product2.getSupplier().getName())
                        && maxSale.equals(dto.totalSold())));

    assertTrue(
        result.stream()
            .anyMatch(
                dto ->
                    dto.product().id().equals(product3.getId())
                        && dto.product().name().equals(product3.getName())
                        && dto.product().price().equals(product3.getPrice())
                        && dto.product().category().equals(product3.getCategory())
                        && dto.product().supplierName().equals(product3.getSupplier().getName())
                        && maxSale.equals(dto.totalSold())));

    verify(salesDetailsRepository, times(1)).findAll();
  }
}
