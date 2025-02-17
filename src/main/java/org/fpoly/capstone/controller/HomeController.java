package org.fpoly.capstone.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
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

        if (user.getRoles().equals(UserRole.ROLE_ADMIN)) {
            log.info("User role admin: {}", user.getRoles());

            return "redirect:/dashboard";
        }

        log.info("User role customer: {}", user.getRoles());

        return "views/hello";
    }
}
