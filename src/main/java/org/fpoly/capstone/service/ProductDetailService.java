package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.productdetail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailRequest;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductDetailService {

    void createProductDetail(ProductDetailRequest request);

    Page<ProductDetailResponse> searchProductDetail(ProductDetailFilterRequest filterRequest, Pageable pageable);

}
