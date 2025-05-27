package org.fpoly.capstone.advice;

import org.fpoly.capstone.common.CommonUtils;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.exceptions.ServiceRuntimeException;
import org.fpoly.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public void addContextModel(Model model) {
        String email = CommonUtils.getPrincipal();
        if (email == null || email.equalsIgnoreCase("anonymousUser")) {
            model.addAttribute("email", null);
            model.addAttribute("fullname", null);
            model.addAttribute("avatar", null);
        } else {
            model.addAttribute("email", email);

            User user = userRepository.findByEmail(email);
            String fullName = (user != null) ? user.getFullName() : null;
            String avatar = (user != null) ? user.getAvatar() : null;
            model.addAttribute("fullName", fullName);
            model.addAttribute("avatar", avatar);
        }
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    @ExceptionHandler(ServiceRuntimeException.class)
    @ResponseBody
    public ResponseEntity<Object> handleServiceRuntimeException(ServiceRuntimeException ex) {
        // You can return the error message in a structured format, e.g., a Map
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // You can choose an appropriate status code
    }

}
