package com.soydz.ptsupermercado.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation error detail")
public record ErrorDTO(
    @Schema(description = "Field that caused the error", example = "price") String field,
    @Schema(description = "Human-readable validation message", example = "must not be null")
        String message) {}
