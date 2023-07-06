package com.cesar.aula04.CustomException;

public class ConvertYearException extends RuntimeException {
  private String message;
  public ConvertYearException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
