package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.repository.MaterialRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.repository.SizeRepository;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.common.FilesStorageService;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MaterialRepository materialRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final FilesStorageService filesStorageService;
    private final ModelMapper modelMapper;
    private static final String PRODUCT_DETAIL_NOT_FOUND_EXCEPTIONS = "Product detail not found with id: ";

//  @Override
//  public Page<ProductDetailResponse> searchProductDetail(ProductDetailFilterRequest filterRequest, Pageable pageable) {
//    Page<ProductDetail> productDetails = this.productDetailRepository.findByFilter(filterRequest, pageable);
//    return productDetails.map(ProductDetailResponse::toDTO);
//  }

    @Override
    public void createProductDetail(ProductDetailRequest request) {
        // Find associated entities and set them in ProductDetail
        Product product = this.productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = this.categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Color color = this.colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found"));
        Material material = this.materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material not found"));
        Size size = this.sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new RuntimeException("Size not found"));

        // Set product detail fields
        ProductDetail productDetail = ProductDetail.builder()
                .product(product)
                .category(category)
                .color(color)
                .material(material)
                .size(size)
                .stockQuantity(request.getStockQuantity())
                .basePrice(request.getBasePrice())
                .build();

        if (request.getStockQuantity() > 0) {
            productDetail.setStatus(true);
        }

        this.productDetailRepository.save(productDetail);


        log.info("Product detail created successfully: {}", productDetail);
    }

//  @Override
//  public void updateProductDetail(Integer productId, ProductDetailRequest request) {
//    ProductDetail productDetail = this.productDetailRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_DETAIL_NOT_FOUND_EXCEPTIONS + productId));
//
//    // Find associated entities and update them in ProductDetail
//    Product product = this.productRepository.findById(request.getProductId().longValue())
//        .orElseThrow(() -> new RuntimeException("Product not found"));
//    Category category = this.categoryRepository.findById(request.getCategoryId().longValue())
//        .orElseThrow(() -> new RuntimeException("Category not found"));
//    Color color = this.colorRepository.findById(request.getColorId().longValue())
//        .orElseThrow(() -> new RuntimeException("Color not found"));
//    Material material = this.materialRepository.findById(request.getMaterialId().longValue())
//        .orElseThrow(() -> new RuntimeException("Material not found"));
//    Size size = this.sizeRepository.findById(request.getSizeId().longValue())
//        .orElseThrow(() -> new RuntimeException("Size not found"));
//
//    // Update product detail fields
//    productDetail.setProduct(product);
//    productDetail.setCategory(category);
//    productDetail.setColor(color);
//    productDetail.setMaterial(material);
//    productDetail.setSize(size);
//    productDetail.setStockQuantity(request.getStockQuantity());
//    productDetail.setBasePrice(request.getBasePrice());
//    productDetail.setStatus(request.getStatus());
//
//    // Update images only if new images are provided
//    if (request.getImages() != null) {
//      List<Image> images = this.filesStorageService.saveAll(request.getImages());
//      productDetail.setImages(images);
//    }
//
//    // Save the updated ProductDetail
//    this.productDetailRepository.save(productDetail);
//  }


//  @Override
//  public void deleteProductDetail(Integer productId) {
//    ProductDetail productDetail = this.productDetailRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_DETAIL_NOT_FOUND_EXCEPTIONS + productId));
//
//    // Delete the product detail
//    this.productDetailRepository.delete(productDetail);
//  }


//  @Override
//  public void changeProductDetailStatus(Integer productId) {
//    ProductDetail productDetail = this.productDetailRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_DETAIL_NOT_FOUND_EXCEPTIONS + productId));
//
//    // Toggle the status
//    productDetail.setStatus(!productDetail.getStatus());
//
//    // Save the updated ProductDetail
//    this.productDetailRepository.save(productDetail);
//  }


//  @Override
//  public ProductDetailResponse getProductDetailById(Integer productId) {
//    ProductDetail productDetail = this.productDetailRepository.findById(productId.longValue())
//        .orElseThrow(() -> new RuntimeException(PRODUCT_DETAIL_NOT_FOUND_EXCEPTIONS + productId));
//
//    // Map ProductDetail to ProductDetailResponse
//    ProductDetailResponse response = new ProductDetailResponse();
//    response.setProductId(productDetail.getProduct().getId());
//    response.setProductCode(productDetail.getProduct().getCode());
//    response.setProductName(productDetail.getProduct().getName());
//    response.setGsmQualification(productDetail.getProduct().getGsmQualification());
//    response.setFeatureImageUrl(productDetail.getProduct().getFeatureImage().getUrl());
//    response.setSizeGuideUrl(productDetail.getProduct().getSizeGuide().getUrl());
//    response.setDescription(productDetail.getProduct().getDescription());
//    response.setCategoryName(productDetail.getCategory().getName());
//    response.setColorName(productDetail.getColor().getName());
//    response.setMaterialName(productDetail.getMaterial().getName());
//    response.setSizeName(productDetail.getSize().getName());
//    response.setBasePrice(productDetail.getBasePrice());
//    response.setStockQuantity(productDetail.getStockQuantity());
//    response.setStatus(productDetail.getStatus());
//
//    // Use ModelMapper to map ProductDetail to ProductDetailResponse
//    return this.modelMapper.map(productDetail, ProductDetailResponse.class);
//  }

}
