package org.fpoly.capstone.exceptions;


import java.io.Serial;

public class ServiceRuntimeException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -2954679423272942965L;

  public ServiceRuntimeException(String message) {
    super(message);
  }

  public ServiceRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
