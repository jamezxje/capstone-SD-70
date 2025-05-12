package org.fpoly.capstone.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@RequiredArgsConstructor
public class ViewExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public String handleAccessDeniedException() {
    return "/views/exception/error-403";
  }

  @ExceptionHandler({NoResourceFoundException.class, NotFoundException.class})
  public String handleNotFoundException() {
    return "/views/exception/error-404";
  }

  @ExceptionHandler(Exception.class)
  public String handleException() {
    return "/views/exception/error-500";
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgumentException(IllegalArgumentException exception, Model model) {
    model.addAttribute("errorMessage", exception.getMessage());
    return "/views/exception/error-500";
  }

}
