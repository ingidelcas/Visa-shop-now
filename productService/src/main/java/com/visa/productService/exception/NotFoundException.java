package com.visa.productService.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
}
