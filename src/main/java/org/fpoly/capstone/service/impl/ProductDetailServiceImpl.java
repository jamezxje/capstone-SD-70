package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.fpoly.capstone.exceptions.ServiceRuntimeException;
import org.fpoly.capstone.repository.BrandRepository;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.repository.ImageRepository;
import org.fpoly.capstone.repository.MaterialRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.repository.SizeRepository;
import org.fpoly.capstone.repository.specification.FrontProductDetailSpecification;
import org.fpoly.capstone.repository.specification.ProductDetailSpecification;
import org.fpoly.capstone.service.ImageService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ModelMapper modelMapper;
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final MaterialRepository materialRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    private static final String PRODUCT_DETAIL_NOT_FOUND_WITH_ID = "Product detail not found with id: ";

    // Helper method giúp tìm entity theo id
    private <T> T findEntityById(Long id, JpaRepository<T, Long> repository, String errorMessage) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(errorMessage + id));
    }

    //Helper method xử lý hình ành
    private void handleImageUpload(ProductDetail productDetail, MultipartFile featureImage, MultipartFile[] images) throws Exception {
        if (featureImage != null && !featureImage.isEmpty()) {
            this.imageService.updateFeatureImageForProductDetail(productDetail, featureImage);
        }

        if (images != null && images.length > 0) {
            if (images[0].getOriginalFilename() != null && !images[0].getOriginalFilename().isEmpty()) {
                this.imageService.deleteImageByProductDetailId(productDetail.getId());
                for (MultipartFile imageFile : images) {
                    if (imageFile != null && !imageFile.isEmpty()) {
                        this.imageService.saveImageToProductDetail(productDetail, imageFile);
                    }
                }
            }
        }
    }

    //Helper method giúp set các thuộc tính vào chi tiết sản phẩm
    private void setCommonProductDetailProperties(ProductDetail productDetail, ProductDetailRequest request) {
        Product product = this.findEntityById(request.getProductId(),
                this.productRepository, "Product not found");
        Brand brand = this.findEntityById(request.getBrandId(),
                this.brandRepository, "Brand not found");
        Color color = this.findEntityById(request.getColorId(),
                this.colorRepository, "Color not found");
        Material material = this.findEntityById(request.getMaterialId(),
                this.materialRepository, "Material not found");
        Size size = this.findEntityById(request.getSizeId(),
                this.sizeRepository, "Size not found");

        BigDecimal price = request.getPrice();
        BigDecimal multiplier = new BigDecimal(1000);

        // Kiểm tra số lượng
        if (request.getQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng không thể nhỏ hơn 0");
        } else if (request.getQuantity() == 0) {
            productDetail.setStatus(ProductVariantStatus.HET_SAN_PHAM); // Giả sử ProductStatus có giá trị HET_SAN_PHAM
            productDetail.setQuantity(0);
        } else {
            productDetail.setQuantity(request.getQuantity());
        }

        productDetail.setProduct(product);
        productDetail.setBrand(brand);
        productDetail.setColor(color);
        productDetail.setMaterial(material);
        productDetail.setSize(size);
        productDetail.setGender(request.getGender());
        productDetail.setPrice(price);
        productDetail.setDescription(request.getDescription());
    }

    private void validateProductDetailRequest(ProductDetailRequest request) {
        ProductDetail existProductDetail =
                this.productDetailRepository.findProductDetailForValidateRequest(request.getProductId(), request.getBrandId(), request.getMaterialId(), request.getSizeId(), request.getGender(), request.getColorId());
        if (existProductDetail != null) {
            throw new ServiceRuntimeException("Chi tiết sản phẩm đã tồn tại");
        }
    }

    @Override

    public List<ProductDetail> getAllProductDetails() {
        return this.productDetailRepository.findAll();
    }

    @Override
    @Transactional
    public void createProductDetail(ProductDetailRequest request) throws Exception {

        this.validateProductDetailRequest(request);

        ProductDetail productDetail = new ProductDetail();

        productDetail.setStatus(ProductVariantStatus.DANG_SU_DUNG);

        this.setCommonProductDetailProperties(productDetail, request);

        this.handleImageUpload(productDetail, request.getFeatureImage(), request.getImages());

        this.productDetailRepository.save(productDetail);
    }

    @Override
    @Transactional
    public void updateProductDetail(Long productDetailId, ProductDetailRequest request) throws Exception {
        ProductDetail existingProductDetail = this.findEntityById(productDetailId,
                this.productDetailRepository, PRODUCT_DETAIL_NOT_FOUND_WITH_ID);

        this.setCommonProductDetailProperties(existingProductDetail, request);

        existingProductDetail.setStatus(request.getStatus());

        this.handleImageUpload(existingProductDetail, request.getFeatureImage(), request.getImages());

        this.productDetailRepository.save(existingProductDetail);
    }

    @Override
    public void deleteProductDetail(Long productDetailId) {
        ProductDetail existingProductDetail = this.findEntityById(productDetailId, this.productDetailRepository, PRODUCT_DETAIL_NOT_FOUND_WITH_ID);

        existingProductDetail.setStatus(ProductVariantStatus.NGUNG_SU_DUNG); //soft delete: chuyển trạng thái sang ngừng sử dụng

        this.productDetailRepository.save(existingProductDetail);
    }

    @Override
    public Page<ProductDetailResponse> getAllProductDetails(Pageable pageable) {
        return this.productDetailRepository.findAll(pageable)
                .map(productDetail -> this.modelMapper.map(productDetail, ProductDetailResponse.class));
    }

    @Override
    public Page<ProductDetailResponse> searchProductDetails(ProductDetailFilterRequest request, Pageable pageable) {
        return this.productDetailRepository.findAll(
                        ProductDetailSpecification
                                .filterByRequest(request), pageable)
                .map(this::convertToResponse);
    }

    private ProductDetailResponse convertToResponse(ProductDetail productDetail) {
        return new ProductDetailResponse(
                productDetail.getId(),
                productDetail.getProduct().getId(),
                productDetail.getProduct().getName(),
                productDetail.getProduct().getCategory().getId(),
                productDetail.getProduct().getCategory().getName(),
                productDetail.getBrand().getId(),
                productDetail.getBrand().getName(),
                productDetail.getSize().getId(),
                productDetail.getSize().getName(),
                productDetail.getColor().getId(),
                productDetail.getColor().getName(),
                productDetail.getMaterial().getId(),
                productDetail.getMaterial().getName(),
                productDetail.getGender(),
                productDetail.getQuantity(),
                productDetail.getPrice(),
                productDetail.getStatus(),
                productDetail.getDescription(),
                productDetail.getFeatureImage()
        );
    }

    @Override
    public ProductDetailResponse getProductDetailById(Long productDetailId) {
        ProductDetailResponse existingProductDetail = this.productDetailRepository.findProductDetailById(productDetailId);
        List<String> imagesUrlList = this.imageService.getImagesUrlByProductDetailId(productDetailId);
        existingProductDetail.setImagesUrl(imagesUrlList);
        return existingProductDetail;
    }

    @Override
    public List<ProductDetailResponse> getAvailableProductDetail() {
        List<ProductDetailResponse> availableProductDetailResponseList =
                this.productDetailRepository.findAllAvailableProductDetail();

        return this.mapProductDetailsToResponse(availableProductDetailResponseList);
    }

    @Override
    public List<ProductDetailResponse> searchAvailableProductDetail(ProductDetailFilterRequest request) {
        List<ProductDetailResponse> productDetailResponseList = this.productDetailRepository.findAll(
                        FrontProductDetailSpecification
                                .filterByRequest(request)).stream().map(this::convertToResponse)
                .toList();

        return this.mapProductDetailsToResponse(productDetailResponseList);
    }

    @Override
    public List<ProductDetailResponse> findRelatedProductDetail(Long productDetailId, Long brandId) {
        List<ProductDetailResponse> relatedProductDetailResponseList =
                this.productDetailRepository.findRelatedProductDetail(productDetailId, brandId, PageRequest.ofSize(10));

        return this.mapProductDetailsToResponse(relatedProductDetailResponseList);
    }

    @Override
    public ProductDetail findProductDetailByIdAndSizeAndColor(Long productId, Long sizeId, Long colorId) {
        ProductDetail existingProductDetail = this.productDetailRepository.findProductDetailByIdAndSizeAndColor(productId, sizeId, colorId);
        return existingProductDetail;
    }

    @Override
    public BigDecimal findProductDetailPriceByIdAndSizeAndColor(Long productId, Long sizeId, Long colorId) {
        log.info("Finding price for ProductId: {}, SizeId: {}, ColorId: {}", productId, sizeId, colorId);
        BigDecimal productDetailPrice = this.productDetailRepository.findProductDetailPriceByIdAndSizeAndColor(productId, sizeId, colorId);

        if (productDetailPrice == null) {
            log.warn("Price not found for ProductId: {}, SizeId: {}, ColorId: {}", productId, sizeId, colorId);
        } else {
            log.info("Price found: {}", productDetailPrice);
        }

        return productDetailPrice;
    }


    private List<ProductDetailResponse> mapProductDetailsToResponse(List<ProductDetailResponse> productDetailResponseList) {
        Map<Long, ProductDetailResponse> groupedProducts = new HashMap<>();

        for (ProductDetailResponse pd : productDetailResponseList) {
            if (!groupedProducts.containsKey(pd.getProductId())) {
                groupedProducts.put(pd.getProductId(), pd);
            }
        }

        return groupedProducts.values().stream()
                .map(response -> this.modelMapper.map(response, ProductDetailResponse.class))
                .toList();
    }

}
