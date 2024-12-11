//package org.fpoly.capstone.service;
//
//import org.fpoly.capstone.service.payload.productdetail.ProductDetailFilterRequest;
//import org.fpoly.capstone.service.payload.productdetail.ProductDetailRequest;
//import org.fpoly.capstone.service.payload.productdetail.ProductDetailResponse;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//public interface ProductDetailService {
//
//  Page<ProductDetailResponse> searchProductDetail(ProductDetailFilterRequest filterRequest, Pageable pageable);
//
//  void createProductDetail(ProductDetailRequest request);
//
//  void updateProductDetail(Integer productId, ProductDetailRequest request);
//
//  void deleteProductDetail(Integer productId);
//
//  void changeProductDetailStatus(Integer productId);
//
//  ProductDetailResponse getProductDetailById(Integer productId);
//
//}
