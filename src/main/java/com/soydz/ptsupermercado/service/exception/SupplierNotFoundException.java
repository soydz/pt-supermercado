package com.soydz.ptsupermercado.service.exception;

public class SupplierNotFoundException extends RuntimeException {

  public SupplierNotFoundException(Long id) {
    super("Supplier with id " + id + " not found");
  }
}
