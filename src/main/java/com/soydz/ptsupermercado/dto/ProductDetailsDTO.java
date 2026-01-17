package com.soydz.ptsupermercado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO for creating a product in a sale")
public record ProductDetailsDTO(
    @Schema(description = "Product ID", example = "17L") @NotNull @Positive Long productId,
    @Schema(description = "Product quantity", example = "2") @NotNull @Min(1) Integer quantity) {}
