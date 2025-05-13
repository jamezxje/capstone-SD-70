package org.fpoly.capstone.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequiredArgsConstructor
public class CommonCotroller {
    private final UserService userService;

    @GetMapping("/hello")
    public String redirectPage(RedirectAttributes redirectAttributes) {
        User user = this.userService.getUserFromContext();

        if (user.getRoles().equals(UserRole.ROLE_ADMIN)) {
            log.info("User role: {}", user.getRoles());
            redirectAttributes.addFlashAttribute("successMessage", "Đăng nhập thành công");
            return "redirect:/dashboard";
        } else if (user.getRoles().equals(UserRole.ROLE_CUSTOMER)) {
            log.info("User role: {}", user.getRoles());
            redirectAttributes.addFlashAttribute("successMessage", "Đăng nhập thành công");
            return "redirect:/";
        }
        return "redirect:/";
    }
}