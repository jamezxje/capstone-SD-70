package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.CartDetail;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CartDetailRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailServiceImpl implements CartDetailService {

    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final ProductDetailRepository productDetailRepository;

    @Override
    public List<CartDetailResponse> findCartDetailByUserId() {
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        Long userId = loggedUser.getId();

        return this.cartDetailRepository.findCartDetailByUserId(userId);
    }

    @Override
    @Transactional
    public void updateCartDetail(CartDetailUpdateRequest request) {

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        ProductDetail productDetail = this.productDetailRepository
                .findById(request.getProductDetailId())
                .orElseThrow(() -> new EntityNotFoundException("Product detail not found with id:" + request.getProductDetailId()));

        if (request.getQuantity() > productDetail.getQuantity()) {
            throw new RuntimeException("Not enough quantity");
        }

        CartDetail cartDetail = this.cartDetailRepository
                .findById(request.getCartDetailId())
                .orElseThrow(() -> new EntityNotFoundException("Cart detail not found with id:" + request.getCartDetailId()));

        if (request.getQuantity() != cartDetail.getQuantity()) {
            cartDetail.setQuantity(request.getQuantity());
            this.cartDetailRepository.save(cartDetail);
        }

    }

    @Override
    public void deleteCartDetail(Long cartDetailId) {
        CartDetail cartDetail = this.cartDetailRepository
                .findById(cartDetailId)
                .orElseThrow(() -> new EntityNotFoundException("Cart detail not found with id:" + cartDetailId));

        this.cartDetailRepository.delete(cartDetail);
    }
}
