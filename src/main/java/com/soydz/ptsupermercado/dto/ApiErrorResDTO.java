package com.soydz.ptsupermercado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.List;

public record ApiErrorResDTO(
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC") Instant timestamp,
    String path,
    String message,
    List<ErrorDTO> errors) {}
