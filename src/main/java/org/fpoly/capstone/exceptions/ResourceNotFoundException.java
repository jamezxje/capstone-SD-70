package org.fpoly.capstone.exceptions;


import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -2954679423272942965L;

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
