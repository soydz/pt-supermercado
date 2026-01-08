package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request DTO for creating or updating Store")
public record StoreReqDTO(
    @Schema(description = "Store name", example = "Primavera Medellin") @NotNull @NotBlank String name,
    @Schema(description = "Store address", example = "calle 50 # 50-01") @NotNull @NotBlank String address,
    @Schema(description = "Store city", example = "Medellin") @NotNull @NotBlank String city) {

  public static Store toEntity(StoreReqDTO storeReqDTO) {
    Store store = new Store();

    store.setName(storeReqDTO.name);
    store.setAddress(storeReqDTO.address);
    store.setCity(storeReqDTO.city);

    return store;
  }
}
