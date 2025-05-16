package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart/api")
@RequiredArgsConstructor
public class CartRestController {

    private final CartDetailService cartDetailService;
    private final UserService userService;

    @GetMapping("/quantity")
    public ResponseEntity<Integer> getCartQuantity() {
        User user = userService.getUserFromContext();
        if (user == null) return ResponseEntity.ok(0);

        int quantity = cartDetailService.findCartDetailByUserId()
                .stream()
                .mapToInt(CartDetailResponse::getQuantity)
                .sum();
        return ResponseEntity.ok(quantity);
    }
}

