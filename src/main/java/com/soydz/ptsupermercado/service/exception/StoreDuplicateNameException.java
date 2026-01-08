package com.soydz.ptsupermercado.service.exception;

public class StoreDuplicateNameException extends RuntimeException {

  public StoreDuplicateNameException(String name) {
    super("Store with name " + name + " already exists");
  }
}
