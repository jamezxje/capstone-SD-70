package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.enum_status.ProductStatus;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.payload.product.ProductFilterRequest;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private static final String PRODUCT_NOT_FOUND_WITH_ID = "Product not found with id: ";

    @Override
    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> getAllActiveProduct() {
        return this.productRepository.findAllActiveProduct();
    }

    @Override
    public Page<ProductResponse> getAllProduct(Pageable pageable) {
        return this.productRepository.findAll(pageable)
                .map(product -> this.modelMapper.map(product, ProductResponse.class));
    }

    @Override
    public Page<ProductResponse> searchProduct(ProductFilterRequest request, Pageable pageable) {
        return this.productRepository.findByFilter(request, pageable);
    }

    @Override
    public void createProduct(ProductRequest request) {
        Category category = this.categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .category(category)
                .code(request.getCode())
                .name(request.getName())
                .status(ProductStatus.DANG_SU_DUNG)
                .build();

        Product savedProduct = this.productRepository.save(product);

        this.modelMapper.map(savedProduct, ProductResponse.class);

    }

    @Override
    public void updateProduct(Long productId, ProductRequest request) {
        Product existingProduct = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND_WITH_ID + productId));

        Category category = this.categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existingProduct.setCategory(category);
        existingProduct.setCode(request.getCode());
        existingProduct.setName(request.getName());
        existingProduct.setStatus(request.getStatus());

        Product updatedProduct = this.productRepository.save(existingProduct);

        this.modelMapper.map(updatedProduct, ProductResponse.class);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product existingProduct = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND_WITH_ID + productId));

        this.productRepository.delete(existingProduct);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product existingProduct = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND_WITH_ID + productId));

        return this.modelMapper.map(existingProduct, ProductResponse.class);
    }

}
