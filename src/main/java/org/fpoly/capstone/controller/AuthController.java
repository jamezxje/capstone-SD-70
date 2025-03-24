package org.fpoly.capstone.controller;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.validation.UserValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(path = "forgot-password")
    public String forgotPassword() {
        return "views/auth/forgot-password";
    }

    @GetMapping(path = "/register")
    public String showSignupPage(Model model) {
        User userRegister = new User();
        Map<String, String> errors = new HashMap<>();
        model.addAttribute("errors", errors);
        model.addAttribute("userRegister", userRegister);
        return "views/auth/sign-up";
    }

    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute("userRegister") User userRegister, Model model) {
        Set<String> userFieldsToValidate = Set.of("fullName", "phoneNumber", "email", "password");
        Map<String, String> errors = UserValidator.validate(userRegister, userFieldsToValidate);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("userRegister", userRegister);
            return "views/auth/sign-up";
        }
        System.out.println("RegisterDTO: " + userRegister);
        userService.createUserRegister(userRegister);
        return "redirect:/auth/login";
    }
}
