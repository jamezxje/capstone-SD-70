package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductDetailService {

    void createProductDetail(ProductDetailRequest request) throws Exception;

    void updateProductDetail(Long productDetailId, ProductDetailRequest request) throws Exception;

    void deleteProductDetail(Long productDetailId);

    Page<ProductDetailResponse> getAllProductDetails(Pageable pageable);

    Page<ProductDetailResponse> searchProductDetails(ProductDetailFilterRequest request, Pageable pageable);

    ProductDetailResponse getProductDetailById(Long productDetailId);

    List<ProductDetailResponse> getAvailableProductDetail();

    List<ProductDetailResponse> findRelatedProductDetail(Long productDetailId, Long brandId);
}
