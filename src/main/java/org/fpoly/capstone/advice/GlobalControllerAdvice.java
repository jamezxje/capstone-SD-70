package org.fpoly.capstone.advice;

import org.fpoly.capstone.common.CommonUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addContextModel(Model model) {
        String email = CommonUtils.getPrincipal();
        if (email == null || email.equalsIgnoreCase("anonymousUser")) {
            model.addAttribute("email", null);
        } else {
            model.addAttribute("email", email);
        }
    }

}
