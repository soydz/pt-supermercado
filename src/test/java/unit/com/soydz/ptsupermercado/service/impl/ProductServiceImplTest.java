package com.soydz.ptsupermercado.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.Supplier;
import com.soydz.ptsupermercado.repository.IProductRepository;
import com.soydz.ptsupermercado.service.interfaces.ISupplierService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock private IProductRepository productRepository;

  @Mock private ISupplierService supplierService;

  @InjectMocks private ProductServiceImpl productService;

  private Product product;
  private ProductReqDTO productReqDTO;

  @BeforeEach
  void setup() {
    Supplier supplier = new Supplier();
    supplier.setId(1L);
    supplier.setName("Granos del tolima");
    supplier.setEmail("clientes@granosdeltolima.com");

    productReqDTO =
        new ProductReqDTO("Arroz la abundancia", "Granos", new BigDecimal(1850), supplier.getId());

    product = ProductReqDTO.toEntity(productReqDTO, supplier);
  }

  @Test
  void shouldReturnProductWhenSaveIsSuccessful() {
    // When
    when(productRepository.save(any(Product.class))).thenReturn(product);

    ProductResDTO result = productService.save(productReqDTO);

    // Then
    assertNotNull(result);
    assertEquals(
        "Arroz la abundancia", result.name(), "The product name does not match the expected value");
    assertEquals(
        "Granos del tolima",
        result.supplierName(),
        "The supplier name does not match the expected value");

    verify(productRepository, times(1)).save(any(Product.class));
  }

  @Test
  void shouldReturnProductResDTOListWhenFindAllIsSuccessful() {
    // when
    when(productRepository.findAll()).thenReturn(List.of(product));

    List<ProductResDTO> result = productService.findAll();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(product.getId(), result.getFirst().id());
    assertEquals(product.getCategory(), result.getFirst().category());
    assertEquals(product.getName(), result.getFirst().name());
    assertEquals(product.getPrice(), result.getFirst().price());
    assertEquals(product.getSupplier().getName(), result.getFirst().supplierName());
  }
}
