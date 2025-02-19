package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.service.payload.product.ProductFilterRequest;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    List<Product> getAllProduct();

    Page<ProductResponse> getAllProduct(Pageable pageable);

    Page<ProductResponse> searchProduct(ProductFilterRequest request, Pageable pageable);

    void createProduct(ProductRequest request);

    void updateProduct(Long productId, ProductRequest request);

    void deleteProduct(Long productId);

    ProductResponse getProductById(Long productId);

}
