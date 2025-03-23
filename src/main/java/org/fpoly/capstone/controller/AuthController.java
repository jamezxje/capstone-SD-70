package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.request.RegisterDTO;
import org.fpoly.capstone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("registerDTO", new RegisterDTO());
        return "views/auth/sign-up";
    }

    @PostMapping("/register/save")
    public String registerUser(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Validation lỗi: " + result.getAllErrors());
            return "views/auth/sign-up";
        }
        System.out.println("RegisterDTO: " + registerDTO);
        userService.registerUser(registerDTO);
        return "redirect:/auth/login";
    }
}
