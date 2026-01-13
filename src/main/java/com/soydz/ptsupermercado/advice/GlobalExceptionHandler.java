package com.soydz.ptsupermercado.advice;

import com.soydz.ptsupermercado.dto.ApiErrorResDTO;
import com.soydz.ptsupermercado.dto.ErrorDTO;
import com.soydz.ptsupermercado.service.exception.ProductNotFoundException;
import com.soydz.ptsupermercado.service.exception.StoreDuplicateNameException;
import com.soydz.ptsupermercado.service.exception.StoreNotFoundException;
import com.soydz.ptsupermercado.service.exception.SupplierNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResDTO> handleValidation(
      MethodArgumentNotValidException ex, HttpServletRequest req) {

    List<ErrorDTO> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> new ErrorDTO(e.getField(), e.getDefaultMessage()))
            .toList();

    return ResponseEntity.badRequest()
        .body(new ApiErrorResDTO(Instant.now(), req.getRequestURI(), "Validation failed", errors));
  }

  @ExceptionHandler({
    ProductNotFoundException.class,
    SupplierNotFoundException.class,
    StoreNotFoundException.class
  })
  public ResponseEntity<ApiErrorResDTO> handleProductEntityNotFound(
      RuntimeException ex, HttpServletRequest req) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiErrorResDTO(Instant.now(), req.getRequestURI(), ex.getMessage(), List.of()));
  }

  @ExceptionHandler(value = StoreDuplicateNameException.class)
  public ResponseEntity<ApiErrorResDTO> handleStoreDuplicateName(
      StoreDuplicateNameException ex, HttpServletRequest req) {

    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ApiErrorResDTO(Instant.now(), req.getRequestURI(), ex.getMessage(), List.of()));
  }
}
