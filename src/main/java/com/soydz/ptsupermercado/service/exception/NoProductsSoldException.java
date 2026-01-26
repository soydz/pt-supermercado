package com.soydz.ptsupermercado.service.exception;

public class NoProductsSoldException extends RuntimeException {

  public NoProductsSoldException() {
    super("No products sold");
  }
}
