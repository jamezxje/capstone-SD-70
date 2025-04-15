package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.entity.CartDetail;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CartDetailRepository;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailUpdateRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartDetailServiceImpl implements CartDetailService {

    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
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
            // Update the quantity of the cart detail
            cartDetail.setQuantity(request.getQuantity());
            this.cartDetailRepository.save(cartDetail);
            log.info("Updated quantity for CartDetail ID: {}, New Quantity: {}", request.getCartDetailId(), request.getQuantity());
        }

        // Get the cart associated with the cart detail
        Cart cart = cartDetail.getCart();
        Set<CartDetail> cartDetails = cart.getCartDetails();

        // Recalculate total price for the cart after the quantity update
        double totalPrice = cartDetails.stream()
                .mapToDouble(detail -> detail.getPrice().doubleValue() * detail.getQuantity())
                .sum();

        // Set the updated total price to the cart
        cart.setTotalPrice(BigDecimal.valueOf(totalPrice));

        try {
            // Attempt to save the updated Cart
            this.cartRepository.save(cart);
            log.info("Updated Cart: ID: {}, User ID: {}, Number of Items: {}, Total Price: {}",
                    cart.getId(), cart.getUser().getId(), cart.getCartDetails().size(), cart.getTotalPrice());
        } catch (Exception e) {
            log.error("Failed to update Cart. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to update Cart", e);
        }
    }


    @Override
    public void deleteCartDetail(Long cartDetailId) {

        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        Cart cart = this.cartRepository.findCartByUserId(loggedUser.getId());

        Set<CartDetail> cartDetailList = cart.getCartDetails();

        CartDetail deleteCartDetail = this.cartDetailRepository
                .findById(cartDetailId)
                .orElseThrow(() -> new EntityNotFoundException("Cart detail not found with id:" + cartDetailId));

        cartDetailList.remove(deleteCartDetail);

        this.cartDetailRepository.delete(deleteCartDetail);

        double totalPrice = cartDetailList.stream()
                .mapToDouble(detail -> detail.getPrice().doubleValue() * detail.getQuantity())
                .sum();

        // Set total price to cart
        cart.setTotalPrice(BigDecimal.valueOf(totalPrice));

        cartRepository.save(cart);

    }
}
