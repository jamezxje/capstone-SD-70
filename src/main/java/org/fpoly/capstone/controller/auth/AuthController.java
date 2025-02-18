package org.fpoly.capstone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "auth")
public class AuthController {

    @GetMapping(path = "login")
    public String showLoginPage() {
        return "views/auth/login";
    }
    
}
