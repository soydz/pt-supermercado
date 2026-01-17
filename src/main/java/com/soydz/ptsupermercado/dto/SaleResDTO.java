package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Sale;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Sale response representation")
public record SaleResDTO(
    @Schema(description = "Unique identifier of the sale", example = "12L") Long id,
    @Schema(
            description = "The date and time when the sale was created",
            example = "2025-03-15T14:30:00")
        LocalDateTime creationDate,
    @Schema(description = "ID of the store where the sale took place", example = "9L") Long storeId,
    @Schema(description = "Name of the store where the sale took place", example = "Primavera")
        String storeName,
    @Schema(description = "List of sales details, including product and quantities sold")
        List<SalesDetailsDTO> salesDetails) {

  public static SaleResDTO fromEntity(Sale sale) {
    return new SaleResDTO(
        sale.getId(),
        sale.getCreationDate(),
        sale.getStore().getId(),
        sale.getStore().getName(),
        sale.getSalesDetails().stream().map(SalesDetailsDTO::fromEntity).toList());
  }
}
