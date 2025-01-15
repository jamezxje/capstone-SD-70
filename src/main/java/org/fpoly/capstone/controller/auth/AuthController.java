package org.fpoly.capstone.controller.auth;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.user.UserModel;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.user.UserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "auth")
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping(path = "login")
    public String showLoginPage() {
        return "views/auth/login";
    }

    @GetMapping(path = "register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserModel());
        return "views/auth/register";
    }

    @PostMapping(path = "register")
    public String register(@ModelAttribute UserModel userModel, RedirectAttributes redirectAttributes) {

        UserRequest request = this.modelMapper.map(userModel, UserRequest.class);

        this.userService.register(request);

        redirectAttributes.addFlashAttribute("message", "Register successfully!");

        return "redirect:/auth/login";
    }

}
