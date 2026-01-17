package com.soydz.ptsupermercado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Request DTO for creating sales")
public record SaleReqDTO(
    @Schema(description = "Store ID", example = "7L") @NotNull Long storeId,
    @Schema(description = "Product list") @NotEmpty @Valid List<ProductDetailsDTO> detail) {}
