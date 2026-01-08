package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Store response representation")
public record StoreResDTO(
    @Schema(description = "Unique identifier of the product", example = "7") Long id,
    @Schema(description = "Store name", example = "Primavera Medellin") String name,
    @Schema(description = "Store address", example = "calle 50 # 50-01") String address,
    @Schema(description = "Store city", example = "Medellin") String city) {

  public static StoreResDTO fromEntity(Store store) {
    return new StoreResDTO(store.getId(), store.getName(), store.getAddress(), store.getCity());
  }
}
