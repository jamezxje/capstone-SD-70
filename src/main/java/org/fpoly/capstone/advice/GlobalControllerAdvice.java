package org.fpoly.capstone.advice;

import org.fpoly.capstone.common.CommonUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

}
