package org.fpoly.capstone.controller.common;

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
<<<<<<< HEAD:src/main/java/org/fpoly/capstone/controller/HomeController.java

        log.info("User role customer: {}", user.getRoles());

        return "PayMentVNPAYSuccess";
=======
        return "redirect:/";
>>>>>>> origin/duytx/dev:src/main/java/org/fpoly/capstone/controller/common/CommonCotroller.java
    }
}
