package com.soydz.ptsupermercado.advice;

import com.soydz.ptsupermercado.dto.ApiErrorResDTO;
import com.soydz.ptsupermercado.dto.ErrorDTO;
import com.soydz.ptsupermercado.service.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
    StoreNotFoundException.class,
    NoProductsSoldException.class
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

  @ExceptionHandler({
    DateTimeParseException.class,
    MethodArgumentTypeMismatchException.class,
    MissingServletRequestParameterException.class
  })
  public ResponseEntity<ApiErrorResDTO> handleValidation(
      RuntimeException ex, HttpServletRequest req) {
    return ResponseEntity.badRequest()
        .body(new ApiErrorResDTO(Instant.now(), req.getRequestURI(), ex.getMessage(), List.of()));
  }
}
