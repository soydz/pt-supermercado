package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Request DTO for creating or updating product")
public record ProductReqDTO(
    @Schema(description = "Product name", example = "Arroz Diana") @NotNull @NotBlank String name,
    @Schema(description = "Product category", example = "Granos") String category,
    @Schema(description = "Product price", example = "7.50") @NotNull @Positive BigDecimal price,
    @Schema(description = "Supplier id", example = "2") Long supplierId) {

  public static Product toEntity(ProductReqDTO productReqDTO, Supplier supplier) {

    Product producto = new Product();
    producto.setName(productReqDTO.name);
    producto.setCategory(productReqDTO.category);
    producto.setPrice(productReqDTO.price);
    producto.setSupplier(supplier);

    return producto;
  }
}
