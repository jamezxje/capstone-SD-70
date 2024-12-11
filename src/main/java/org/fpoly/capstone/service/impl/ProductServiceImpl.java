//package org.fpoly.capstone.service.impl;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.fpoly.capstone.controller.payload.product.ProductViewModel;
//import org.fpoly.capstone.entity.Image;
//import org.fpoly.capstone.entity.Product;
//import org.fpoly.capstone.repository.ProductRepository;
//import org.fpoly.capstone.service.ProductService;
//import org.fpoly.capstone.service.common.FilesStorageService;
//import org.fpoly.capstone.service.payload.product.ProductRequest;
//import org.fpoly.capstone.service.payload.product.ProductResponse;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Log4j2
//@Service
//@RequiredArgsConstructor
//public class ProductServiceImpl implements ProductService {
//
//  private final ProductRepository productRepository;
//  private final ModelMapper modelMapper;
//  private final FilesStorageService filesStorageService;
//  private static final String PRODUCT_NOT_FOUND_EXCEPTIONS = "Product not found with id: ";
//
//  // Constructor (or other method) to configure the ModelMapper
//  @PostConstruct
//  public void init() {
//    this.modelMapper.typeMap(ProductResponse.class, ProductViewModel.class).addMappings(mapper -> {
//      mapper.map(ProductResponse::getFeatureImageUrl, ProductViewModel::setFeatureImageUrl);
//      mapper.map(ProductResponse::getSizeGuideUrl, ProductViewModel::setSizeGuideUrl);
//    });
//  }
//
//  @Override
//  public List<Product> getAllProduct() {
//    return this.productRepository.findAll();
//  }
//
//  @Override
//  public Page<ProductResponse> getAllProduct(Pageable pageable) {
//    return this.productRepository.findAll(pageable)
//        .map(product -> this.modelMapper.map(product, ProductResponse.class));
//  }
//
//  @Override
//  public Page<ProductResponse> searchProduct(String code, String name, String weight, String gsmQualification, Boolean status, Pageable pageable) {
//    if (code == null && name == null && weight == null && gsmQualification == null && status == null) {
//      return this.productRepository.findAllResponseProduct(pageable);
//    }
//
//    return this.productRepository.findByFilter(code, name, weight, gsmQualification, status, pageable);
//  }
//
//  @Override
//  public void createProduct(ProductRequest request) {
//    Image featureImage = this.filesStorageService.save(request.getFeatureImage());
//    Image sizeGuideImage = this.filesStorageService.save(request.getSizeGuideImage());
//
//    Optional<Product> existingProduct = this.productRepository.findByCode(request.getCode());
//    if (existingProduct.isPresent()) {
//      throw new IllegalArgumentException("Product code already exists!");
//    }
//
//    Product product = Product.builder()
//        .code(request.getCode())
//        .name(request.getName())
//        .weight(request.getWeight())
//        .gsmQualification(request.getGsmQualification())
//        .featureImage(featureImage)
//        .description(request.getDescription())
//        .sizeGuide(sizeGuideImage)
//        .status(request.getStatus())
//        .build();
//
//    this.productRepository.save(product);
//
//    log.info("Product created successfully: {}", product);
//  }
//
//  @Override
//  public void updateProduct(Integer productId, ProductRequest request) {
//    Product product = this.productRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_EXCEPTIONS + productId));
//
//    Image featureImage = this.filesStorageService.save(request.getFeatureImage());
//    Image sizeGuideImage = this.filesStorageService.save(request.getSizeGuideImage());
//
//    product.setCode(request.getCode());
//    product.setName(request.getName());
//    product.setWeight(request.getWeight());
//    product.setGsmQualification(request.getGsmQualification());
//    product.setFeatureImage(featureImage != null ? featureImage : product.getFeatureImage());
//    product.setDescription(request.getDescription());
//    product.setSizeGuide(sizeGuideImage != null ? sizeGuideImage : product.getSizeGuide());
//    product.setStatus(request.getStatus());
//
//    this.productRepository.save(product);
//  }
//
//  @Override
//  public void deleteProduct(Integer productId) {
//    Product product = this.productRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_EXCEPTIONS + productId));
//
//    this.filesStorageService.delete(product.getFeatureImage());
//    this.filesStorageService.delete(product.getSizeGuide());
//
//    this.productRepository.delete(product);
//  }
//
//  @Override
//  public void changeProductStatus(Integer productId) {
//    Product product = this.productRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_EXCEPTIONS + productId));
//
//    Boolean productStatus = product.getStatus();
//
//    product.setStatus(!productStatus);
//
//    this.productRepository.save(product);
//  }
//
//  @Override
//  public ProductResponse getProductById(Integer productId) {
//    Product product = this.productRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND_EXCEPTIONS + productId));
//
//    return this.modelMapper.map(product, ProductResponse.class);
//  }
//
//}
