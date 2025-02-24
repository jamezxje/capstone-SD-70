package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.fpoly.capstone.repository.BrandRepository;
import org.fpoly.capstone.repository.CategoryRepository;
import org.fpoly.capstone.repository.ColorRepository;
import org.fpoly.capstone.repository.MaterialRepository;
import org.fpoly.capstone.repository.ProductDetailRepository;
import org.fpoly.capstone.repository.ProductRepository;
import org.fpoly.capstone.repository.SizeRepository;
import org.fpoly.capstone.service.ImageService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

    private final ModelMapper modelMapper;
    private final ProductDetailRepository productDetailRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final MaterialRepository materialRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ImageService imageService;
    private static final String PRODUCT_DETAIL_NOT_FOUND_WITH_ID = "Product detail not found with id: ";

    @Override
    @Transactional
    public void createProductDetail(ProductDetailRequest request) throws Exception {
        Product product = this.productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Brand brand = this.brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
        Color color = this.colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new EntityNotFoundException("Color not found"));
        Material material = this.materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));
        Size size = this.sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new EntityNotFoundException("Size not found"));

        ProductDetail productDetail = new ProductDetail();
        productDetail.setProduct(product);
        productDetail.setBrand(brand);
        productDetail.setColor(color);
        productDetail.setMaterial(material);
        productDetail.setSize(size);
        productDetail.setGender(request.getGender());
        productDetail.setQuantity(request.getQuantity());
        productDetail.setPrice(request.getPrice());
        productDetail.setStatus(ProductVariantStatus.DANG_SU_DUNG);
        productDetail.setDescription(request.getDescription());

        // Lưu ảnh thumbnail
        this.imageService.updateFeatureImageForProductDetail(productDetail, request.getFeatureImage());

        // Lưu các ảnh chi tiết
        for (MultipartFile imageFile : request.getImages()) {
            this.imageService.saveImageToProductDetail(productDetail, imageFile);
        }

        // Lưu sản phẩm chi tiết vào cơ sở dữ liệu
        this.productDetailRepository.save(productDetail);
    }

    @Override
    public void updateProductDetail(Long productDetailId, ProductDetailRequest request) throws Exception {
        ProductDetail productDetail = this.productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new EntityNotFoundException("Product detail not found"));

        Product product = this.productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Brand brand = this.brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
        Color color = this.colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new EntityNotFoundException("Color not found"));
        Material material = this.materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new EntityNotFoundException("Material not found"));
        Size size = this.sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new EntityNotFoundException("Size not found"));

        productDetail.setProduct(product);
        productDetail.setBrand(brand);
        productDetail.setColor(color);
        productDetail.setMaterial(material);
        productDetail.setSize(size);
        productDetail.setGender(request.getGender());
        productDetail.setQuantity(request.getQuantity());
        productDetail.setPrice(request.getPrice());
        productDetail.setStatus(request.getStatus());
        productDetail.setDescription(request.getDescription());

        if (request.getFeatureImage() != null && !request.getFeatureImage().isEmpty()) {
            // Handle the image upload logic here
            this.imageService.updateFeatureImageForProductDetail(productDetail, request.getFeatureImage());

        }

        if (request.getImages() != null && request.getImages().length > 0) {
            // Handle multiple image upload logic here
            for (MultipartFile imageFile : request.getImages()) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    this.imageService.saveImageToProductDetail(productDetail, imageFile);
                }
            }
        }

        this.productDetailRepository.save(productDetail);

    }

    @Override
    public void deleteProductDetail(Long productDetailId) {
        ProductDetail productDetail = this.productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new EntityNotFoundException("Product detail not found"));

        productDetail.setStatus(ProductVariantStatus.NGUNG_SU_DUNG);

        this.productDetailRepository.save(productDetail);
    }

    @Override
    public Page<ProductDetailResponse> getAllProductDetails(Pageable pageable) {
        return this.productDetailRepository.findAll(pageable)
                .map(productDetail -> this.modelMapper.map(productDetail, ProductDetailResponse.class));
    }

    @Override
    public Page<ProductDetailResponse> searchProductDetails(ProductDetailFilterRequest request, Pageable pageable) {
        Page<ProductDetailResponse> page = this.productDetailRepository.findByFilter(request, pageable);
        page.forEach(response -> {
            List<String> images = this.productDetailRepository.findImagesByProductDetailId(response.getId());
            response.setImagesUrl(images);
        });
        return page;
    }

    @Override
    public ProductDetailResponse getProductDetailById(Long productDetailId) {
        ProductDetailResponse existingProductDetail = this.productDetailRepository.findProductDetailById(productDetailId);
        List<String> imagesUrlList = this.imageService.getImagesUrlByProductDetailId(productDetailId);
        existingProductDetail.setImagesUrl(imagesUrlList);
        return existingProductDetail;
    }

}
