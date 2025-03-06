package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CartDetailRepository;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailServiceImpl implements CartDetailService {

    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;

    @Override
    public List<CartDetailResponse> findCartDetailByUserId() {
        // get logged customer
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        Long userId = loggedUser.getId();

        return cartDetailRepository.findCartDetailByUserId(userId);
    }
}
