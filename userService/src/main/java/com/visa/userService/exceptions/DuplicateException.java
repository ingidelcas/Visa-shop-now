package com.visa.userService.exceptions;

public class DuplicateException extends RuntimeException {

  public DuplicateException(String message) {
    super(message);
  }
}
