package org.fpoly.capstone.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.validation.UserValidator;
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

    @GetMapping(path = "login")
    public String showLoginPage() {
        return "views/auth/login";
    }

    @GetMapping(path = "login/online")
    public String showLoginForOnlineUserPage() {
        return "/views/user-online-view/auth/login";
    }


    @GetMapping(path = "register/online")
    public String registerUserOnline(Model model) {
        User userRegister = new User();
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors", errors);
        model.addAttribute("userRegister", userRegister);
        return "/views/user-online-view/auth/sign-up";
    }

    @PostMapping("/register/online/save")
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
    //--------------------------------------------------------------------
    @GetMapping(path = "/register")
    public String showSignupPage(Model model) {
        User userRegister = new User();
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors", errors);
        model.addAttribute("userRegister", userRegister);
        return "views/auth/sign-up";
    }

    @PostMapping("/register/save")
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

    @GetMapping(path = "/forgot-password")
    public String forgotPasswordPage(Model model, @ModelAttribute("error") String error) {
        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "views/auth/forgot-password";
    }

    @PostMapping(path = "/forgot-password/save")
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
}
