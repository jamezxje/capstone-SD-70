package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;

import java.util.List;

public interface CartDetailService {

    List<CartDetailResponse> findCartDetailByUserId();

}
