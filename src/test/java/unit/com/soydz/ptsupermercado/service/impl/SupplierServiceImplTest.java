package com.soydz.ptsupermercado.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.soydz.ptsupermercado.entity.Supplier;
import com.soydz.ptsupermercado.repository.ISupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

  private Supplier supplier;

  @Mock private ISupplierRepository supplierRepository;

  @InjectMocks private SupplierServiceImpl supplierService;

  @BeforeEach
  void setup() {
    supplier = new Supplier();
    supplier.setId(2L);
    supplier.setName("Lácteos la colina");
    supplier.setEmail("clientes@lacteoscolina.com");
  }

  @Test
  void shouldReturnSupplierWhenFindByIdSuccessful() {
    // When
    when(supplierRepository.findById(any())).thenReturn(Optional.of(supplier));

    Supplier result = supplierService.findById(2L);

    // Then
    assertNotNull(result);
    assertEquals(
        "clientes@lacteoscolina.com",
        result.getEmail(),
        "The supplier email does not match the expected value");
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenFindByIdFails() {
    // When
    when(supplierRepository.findById(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(EntityNotFoundException.class, () -> supplierService.findById(5L));
  }
}
