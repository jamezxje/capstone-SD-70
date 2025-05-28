package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart/api")
@RequiredArgsConstructor
public class CartRestController {

    private final CartDetailService cartDetailService;
    private final UserService userService;

    // API trả về tổng số lượng sản phẩm trong giỏ hàng
    @GetMapping("/quantity")
    public ResponseEntity<Integer> getCartQuantity() {
        User user = userService.getUserFromContext();
        if (user == null) {
            return ResponseEntity.ok(0);
        }

        List<CartDetailResponse> cartDetails = cartDetailService.findCartDetailByUserId();
        int totalQuantity = cartDetails.stream()
                .mapToInt(CartDetailResponse::getQuantity)
                .sum();

        return ResponseEntity.ok(totalQuantity);
    }
}

