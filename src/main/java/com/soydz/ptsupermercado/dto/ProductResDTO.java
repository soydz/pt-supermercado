package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Product response representation")
public record ProductResDTO(
    @Schema(description = "Unique identifier of the product", examples = "13") Long id,
    @Schema(description = "Product name", example = "Arroz floral") String name,
    @Schema(description = "product category", example = "Granos") String category,
    @Schema(description = "Product price", example = "10.50") BigDecimal price,
    @Schema(description = "Supplier name", example = "Granos del sol") String supplierName) {

  public static ProductResDTO fromEntity(Product producto) {

    return new ProductResDTO(
        producto.getId(),
        producto.getName(),
        producto.getCategory(),
        producto.getPrice(),
        producto.getSupplier().getName());
  }
}
