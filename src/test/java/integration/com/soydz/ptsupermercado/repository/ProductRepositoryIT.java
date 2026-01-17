package com.soydz.ptsupermercado.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.Supplier;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryIT {

  @Container
  private static final JdbcDatabaseContainer<?> databaseContainer =
      new PostgreSQLContainer<>("postgres:16.11-alpine")
          .withDatabaseName("test_db")
          .withUsername("user")
          .withPassword("pass");

  @DynamicPropertySource
  private static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
    registry.add("spring.datasource.username", databaseContainer::getUsername);
    registry.add("spring.datasource.password", databaseContainer::getPassword);
  }

  @Autowired private ISupplierRepository supplierRepository;
  @Autowired private IProductRepository productRepository;
  @Autowired private EntityManager entityManager;

  @Test
  void shouldSaveProductWithSupplier() {
    // Given
    Supplier supplier =
        supplierRepository.save(new Supplier("granos sabana", "clientes@granossabana.com"));

    // When
    Product saved =
        productRepository.saveAndFlush(
            new Product("Arroz floral", "granos", BigDecimal.valueOf(15), supplier, List.of()));

    entityManager.clear();

    Product productFound = productRepository.findById(saved.getId()).orElseThrow();
    Supplier supplierFound = supplierRepository.findById(supplier.getId()).orElseThrow();

    // Then
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getSupplier().getName()).isEqualTo(supplier.getName());
    assertThat(productFound.getName()).isEqualTo(saved.getName());
    assertThat(supplierFound.getEmail()).isEqualTo(supplier.getEmail());
    assertThat(productFound.getSupplier().getName()).isEqualTo(supplierFound.getName());
  }
}
