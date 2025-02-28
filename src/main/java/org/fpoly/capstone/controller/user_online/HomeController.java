package org.fpoly.capstone.controller.user_online;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(path = "")
    public String onOpenUserHomeView() {
        return "/views/user-online-view/index";
    }
}
