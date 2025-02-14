package org.fpoly.capstone.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.controller.payload.product.ProductViewModel;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.common.FilesStorageService;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final FilesStorageService filesStorageService;
    private final ProductDetailService productDetailService;
    private static final String PRODUCT_NOT_FOUND_EXCEPTIONS = "Product not found with id: ";

    // Constructor (or other method) to configure the ModelMapper
    @PostConstruct
    public void init() {
        this.modelMapper.typeMap(ProductResponse.class, ProductViewModel.class).addMappings(mapper -> {
            mapper.map(ProductResponse::getFeatureImageUrl, ProductViewModel::setFeatureImageUrl);
            mapper.map(ProductResponse::getSizeGuideUrl, ProductViewModel::setSizeGuideUrl);
        });
    }

    @Override
    public void createProduct(ProductRequest request) {
        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .status(Boolean.TRUE)
                .build();

        Product savedProduct = this.productRepository.save(product);

        for (ProductRequest.ProductDetailRequest variantRequest : request.getVariants()) {
            variantRequest.setProductId(savedProduct.getId());
            ProductDetailRequest productDetailRequest = this.modelMapper.map(variantRequest, ProductDetailRequest.class);
            this.productDetailService.createProductDetail(productDetailRequest);
        }

        log.info("Product created successfully: {}", product);
    }


}
