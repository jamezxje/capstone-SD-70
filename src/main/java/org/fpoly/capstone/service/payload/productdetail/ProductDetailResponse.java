package org.fpoly.capstone.service.payload.productdetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.Image;
import org.fpoly.capstone.entity.ProductDetail;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailResponse {

    private Long productDetailId;
    private Long productId;
    private String productCode;
    private String productName;
    private String productWeight;
    private String gsmQualification;
    private String featureImageUrl;
    private String sizeGuideUrl;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private Long colorId;
    private String colorName;
    private Long materialId;
    private String materialName;
    private Long sizeId;
    private String sizeName;
    private Double basePrice;
    private Long stockQuantity;
    private List<Image> images;
    private List<String> imageUrls;
    private Boolean status;

    public static ProductDetailResponse toDTO(ProductDetail productDetail) {
        return ProductDetailResponse.builder()
                .productDetailId(productDetail.getId())
                .productId(productDetail.getProduct().getId())
                .productCode(productDetail.getProduct().getCode())
                .productName(productDetail.getProduct().getName())
                .featureImageUrl(productDetail.getFeatureImage().getUrl())
//                .sizeGuideUrl(productDetail.getProduct().getSizeGuide().getUrl())
                .description(productDetail.getDescription())
                .categoryId(productDetail.getCategory().getId())
                .categoryName(productDetail.getCategory().getName())
                .brandId(productDetail.getBrand().getId())
                .brandName(productDetail.getBrand().getName())
                .colorId(productDetail.getColor().getId())
                .colorName(productDetail.getColor().getName())
                .materialId(productDetail.getMaterial().getId())
                .materialName(productDetail.getMaterial().getName())
                .sizeId(productDetail.getSize().getId())
                .sizeName(productDetail.getSize().getName())
                .basePrice(productDetail.getBasePrice())
                .stockQuantity(productDetail.getStockQuantity())
                .status(productDetail.getStatus())
                .build();
    }
}
