package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Product;
import java.math.BigDecimal;

public record ProductResDTO(
    Long id, String name, String category, BigDecimal price, String supplierName) {

  public static ProductResDTO fromEntity(Product producto) {

    return new ProductResDTO(
        producto.getId(),
        producto.getName(),
        producto.getCategory(),
        producto.getPrice(),
        producto.getSupplier().getName());
  }
}
