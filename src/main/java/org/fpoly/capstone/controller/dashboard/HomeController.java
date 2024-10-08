package org.fpoly.capstone.controller.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.type.Role;
import org.fpoly.capstone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController {

  private final UserService userService;

  @GetMapping("/hello")
  public String redirectPage() {
    User user = this.userService.getUserFromContext();

    if (user.getRole().equals(Role.ROLE_ADMIN)) {
      log.info("User role admin: {}", user.getRole());

      return "redirect:/dashboard";
    }

    log.info("User role customer: {}", user.getRole());

    return "hello";
  }
}
