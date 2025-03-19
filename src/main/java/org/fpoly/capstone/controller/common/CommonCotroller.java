package org.fpoly.capstone.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.config.UserDetailsCustom;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CommonCotroller {
    private final UserService userService;

    @GetMapping("/hello")
    public String redirectPage() {
        User user = this.userService.getUserFromContext();

        if (user.getRoles().equals(UserRole.ROLE_ADMIN)) {
            log.info("User role: {}", user.getRoles());

            return "redirect:/dashboard";
        } else if (user.getRoles().equals(UserRole.ROLE_CUSTOMER)) {
            log.info("User role: {}", user.getRoles());

            return "redirect:/";
        }
        return "redirect:/";
    }
}
