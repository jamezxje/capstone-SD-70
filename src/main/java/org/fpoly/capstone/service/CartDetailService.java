package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailUpdateRequest;

import java.util.List;

public interface CartDetailService {

    List<CartDetailResponse> findCartDetailByUserId();

    void updateCartDetail(CartDetailUpdateRequest request);

}
