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
import org.fpoly.capstone.service.CartService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.cart.AddProductToCartRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailService productDetailService;

    @Override
    @Transactional
    public void addToCart(AddProductToCartRequest request) {
        // get logged customer
        User loggedUser = this.userService.getUserFromContext();

        if (loggedUser == null) {
            throw new EntityNotFoundException("User not found");
        }

        //find product detail by productId and sizeId from request
        ProductDetail productDetailRequest = this.productDetailService
                .findProductDetailByIdAndSize(request.getProductId(), request.getSizeId());

        if (productDetailRequest == null) {
            log.error("Product detail not found for Product ID: {} and Size ID: {}", request.getProductId(), request.getSizeId());
            throw new EntityNotFoundException("Product detail not found for the given product and size.");
        }

        //check if quantity from request is valid or not
        if (request.getQuantity() > productDetailRequest.getQuantity()) {
            log.error("Not enough product quantity: {}",
                    request.getQuantity());
            throw new RuntimeException("Not enough quantity");
        }

        Cart cart = this.cartRepository.findCartByUserId(loggedUser.getId());

        if (cart == null) {
            cart = new Cart();
        }

        Set<CartDetail> cartDetails = cart.getCartDetails();
        if (cartDetails == null) {
            cartDetails = new HashSet<>();
        }

        //loop through all cart detail to see if the product exist or not
        CartDetail existingCartDetail = cartDetails.stream()
                .filter(detail -> detail.getProductDetail().getId().equals(productDetailRequest.getId()))
                .findFirst()
                .orElse(null);

        //if the product is firt added to cart
        if (existingCartDetail == null) {
            existingCartDetail = new CartDetail();
            existingCartDetail.setProductDetail(productDetailRequest);
            existingCartDetail.setQuantity(request.getQuantity());
            existingCartDetail.setPrice(productDetailRequest.getPrice());
            existingCartDetail.setCart(cart);
            cartDetails.add(existingCartDetail);
            this.cartDetailRepository.save(existingCartDetail);
            log.info("Added new product to cart. Product ID: {}, Size ID: {}, Quantity: {}", request.getProductId(), request.getSizeId(), request.getQuantity());
        } else {
            //if the product already added to cart => update its quantity only
            existingCartDetail.setQuantity(existingCartDetail.getQuantity() + request.getQuantity());
            this.cartDetailRepository.save(existingCartDetail);
            log.info("Updated existing product in cart. Product ID: {}, Size ID: {}, New Quantity: {}",
                    request.getProductId(), request.getSizeId(), existingCartDetail.getQuantity());
        }

        try {
            // Attempt to save Cart
            cart.setCartDetails(cartDetails);
            cart.setUser(loggedUser);
            this.cartRepository.save(cart);
            log.info("Saved Cart: ID: {}, User ID: {}, Number of Items: {}",
                    cart.getId(), cart.getUser().getId(), cart.getCartDetails().size());
        } catch (Exception e) {
            log.error("Failed to save Cart. Error: {}", e.getMessage());
            throw new RuntimeException("Failed to save Cart", e);
        }

    }

}
