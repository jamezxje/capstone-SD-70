package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

  List<Product> getAllProduct();

  Page<ProductResponse> getAllProduct(Pageable pageable);

  Page<ProductResponse> searchProduct(String code, String name, String weight, String gsmQualification, Boolean status, Pageable pageable);

  void createProduct(ProductRequest request);

  void updateProduct(Integer productId, ProductRequest request);

  void deleteProduct(Integer productId);

  void changeProductStatus(Integer productId);

  ProductResponse getProductById(Integer productId);

}
