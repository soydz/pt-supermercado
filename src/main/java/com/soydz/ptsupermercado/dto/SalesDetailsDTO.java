package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.SalesDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO representing the details of a product included in a sale")
public record SalesDetailsDTO(
    @Schema(description = "Quantity of the product sold", example = "3") Integer quantity,
    @Schema(description = "Name of the product sold", example = "Arroz premium") String productName,
    @Schema(description = "Unit price of the product at the time of sale", example = "3850")
        BigDecimal productPrice) {

  public static SalesDetailsDTO fromEntity(SalesDetails salesDetails) {
    return new SalesDetailsDTO(
        salesDetails.getQuantity(),
        salesDetails.getProduct().getName(),
        salesDetails.getProduct().getPrice());
  }
}
