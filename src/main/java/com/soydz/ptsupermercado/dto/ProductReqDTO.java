package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.Supplier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductReqDTO(
    @NotNull @NotBlank String name,
    String category,
    @NotNull @Positive BigDecimal price,
    Long supplierId) {

  public static Product toEntity(ProductReqDTO productReqDTO, Supplier supplier) {

    Product producto = new Product();
    producto.setName(productReqDTO.name);
    producto.setCategory(productReqDTO.category);
    producto.setPrice(productReqDTO.price);
    producto.setSupplier(supplier);

    return producto;
  }
}
