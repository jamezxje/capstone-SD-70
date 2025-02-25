package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductStatus;
import org.fpoly.capstone.exceptions.ResourceNotFoundException;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.service.ImageService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.ProductService;
import org.fpoly.capstone.service.payload.product.ProductFilterRequest;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductDetailService productDetailService;
    private final ImageService imageService;
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
    public void createProduct(ProductRequest request) throws Exception {
        Category category = this.categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .category(category)
                .code(request.getCode())
                .name(request.getName())
                .status(ProductStatus.DANG_SU_DUNG)
                .build();

        Product savedProduct = this.productRepository.save(product);

        List<ProductRequest.ProductDetailRequest> productDetailRequestList = request.getProductVariantList();
        Long brandVariantId = productDetailRequestList.get(0).getBrandId();
        Long materialVariantId = productDetailRequestList.get(0).getMaterialId();
        Gender genderVariant = productDetailRequestList.get(0).getGender();
        String descriptionVariant = productDetailRequestList.get(0).getDescription();
        Long colorVariantId = productDetailRequestList.get(0).getColorId();
        MultipartFile featureImageVariant = productDetailRequestList.get(0).getFeatureImage();
        MultipartFile[] imagesVariant = productDetailRequestList.get(0).getImages();

        for (ProductRequest.ProductDetailRequest variantRequest : request.getProductVariantList()) {
            variantRequest.setProductId(savedProduct.getId());
            variantRequest.setBrandId(brandVariantId);
            variantRequest.setMaterialId(materialVariantId);
            variantRequest.setGender(genderVariant);
            variantRequest.setDescription(descriptionVariant);
            variantRequest.setColorId(colorVariantId);
            variantRequest.setFeatureImage(featureImageVariant);
            variantRequest.setImages(imagesVariant);
            ProductDetailRequest productDetailRequest = this.modelMapper.map(variantRequest, ProductDetailRequest.class);
            this.productDetailService.createProductDetail(productDetailRequest);
        }

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
