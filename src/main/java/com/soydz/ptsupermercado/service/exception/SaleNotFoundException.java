package com.soydz.ptsupermercado.service.exception;

public class SaleNotFoundException extends RuntimeException {

  public SaleNotFoundException(Long id) {
    super("Sale with id " + id + " not found");
  }
}
