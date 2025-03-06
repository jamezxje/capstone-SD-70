package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.cart.AddProductToCartRequest;

public interface CartService {
    void addToCart(AddProductToCartRequest request);

}
