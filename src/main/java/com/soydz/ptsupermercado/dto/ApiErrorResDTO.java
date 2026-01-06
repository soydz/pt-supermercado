package com.soydz.ptsupermercado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

public record ApiErrorResDTO(
    @Schema(example = "2026-01-05T17:55:06Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
        Instant timestamp,
    @Schema(example = "/api/v1/productos/25") String path,
    @Schema(example = "Product with id 25 not found") String message,
    @Schema(description = "Validation errors") List<ErrorDTO> errors) {}
