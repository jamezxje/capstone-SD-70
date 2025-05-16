package org.fpoly.capstone.controller.user_online;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

    private final CartDetailService cartDetailService;
    private final UserService userService;

    @ModelAttribute("cartQuantity")
    public Integer getCartQuantity() {
        User loggedUser = userService.getUserFromContext();
        if (loggedUser == null) {
            return null; // trả về null thay vì 0 để dễ kiểm tra ở giao diện
        }
        //hiển thị số lượng trên cart
        return cartDetailService.findCartDetailByUserId()
                .stream()
                .mapToInt(CartDetailResponse::getQuantity)
                .sum();
    }
}

