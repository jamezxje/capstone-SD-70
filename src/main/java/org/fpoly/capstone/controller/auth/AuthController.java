package org.fpoly.capstone.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.validation.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(path = "login")
    public String showLoginPage() {
        return "views/auth/login";
    }

    @GetMapping(path = "login/online")
    public String showLoginForOnlineUserPage() {
        return "/views/user-online-view/auth/login";
    }

    @GetMapping(path = "/register")
    public String showSignupPage(Model model) {
        User userRegister = new User();
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors", errors);
        model.addAttribute("userRegister", userRegister);
        return "views/auth/sign-up";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userRegister") User userRegister, Model model, RedirectAttributes redirectAttributes) {
        Set<String> userFieldsToValidate = Set.of("fullName", "phoneNumber", "email", "password");
        Map<String, String> errors = UserValidator.validate(userRegister, userFieldsToValidate);
        if (userService.existsByEmail(userRegister.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (userService.existsByPhoneNumber(userRegister.getPhoneNumber())) {
            errors.put("phoneNumber", "Số điện thoại đã tồn tại!");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("userRegister", userRegister);
            return "views/auth/sign-up";
        }
        System.out.println("RegisterDTO: " + userRegister);
        userService.createUserRegister(userRegister);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công!");
        return "redirect:/auth/login";
    }
    @GetMapping(path = "/change/online")
    public String changeUserOnline(Model model) {
        User loggedUser = this.userService.getUserFromContext();
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors", errors);
        model.addAttribute("loggedUser", loggedUser);
        return "views/auth/change-password";
    }

    @PostMapping("/change/online")
    public String processChangePassword(@RequestParam("currentPassword") String currentPassword,
                                        @RequestParam("newPassword") String newPassword,
                                        @RequestParam("confirmPassword") String confirmPassword,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {

        // Gọi service để lấy Map lỗi
        Map<String, String> result = userService.changeUserPassword(currentPassword, newPassword, confirmPassword);

        if (result.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công.");
            return "redirect:/auth/change/online";
        }  else {
            model.addAttribute("errors", result);
            model.addAttribute("currentPassword", currentPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("confirmPassword", confirmPassword);
            model.addAttribute("loggedUser", userService.getUserFromContext());
            return "views/auth/change-password";
        }
    }

    @GetMapping(path = "/forgot-password")
    public String forgotPasswordPage(Model model, @ModelAttribute("error") String error) {
        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "views/auth/forgot-password";
    }

    @PostMapping(path = "/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        String responseMessage = userService.processForgotPassword(email);

        if (responseMessage.startsWith("Email không tồn tại")) {
            redirectAttributes.addFlashAttribute("error", responseMessage);
            return "redirect:/auth/forgot-password";
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Vui lòng kiểm tra email của bạn!");
            return "redirect:/auth/login";
        }
    }

    //--------------------------------------------------------------------

    @GetMapping(path = "register/online")
    public String registerUserOnline(Model model) {
        User userRegister = new User();
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors", errors);
        model.addAttribute("userRegister", userRegister);
        return "/views/user-online-view/auth/sign-up";
    }

    @PostMapping("/register/online")
    public String registerUserOnlineSave(@ModelAttribute("userRegister") User userRegister, Model model, RedirectAttributes redirectAttributes) {
        Set<String> userFieldsToValidate = Set.of("fullName", "phoneNumber", "email", "password");
        Map<String, String> errors = UserValidator.validate(userRegister, userFieldsToValidate);
        if (userService.existsByEmail(userRegister.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (userService.existsByPhoneNumber(userRegister.getPhoneNumber())) {
            errors.put("phoneNumber", "Số điện thoại đã tồn tại!");
        }
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("userRegister", userRegister);
            return "/views/user-online-view/auth/sign-up";
        }
        System.out.println("RegisterDTO: " + userRegister);
        userService.createUserRegister(userRegister);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công!");
        return "redirect:/auth/login/online";
    }

}
