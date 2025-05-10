package org.fpoly.capstone.service.payload.product_detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.Image;
import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailRequest {

    private Long id;
    private Long productId;
    private Long brandId;
    private Long colorId;
    private Long materialId;
    private Long sizeId;
    private Gender gender;
    private Integer quantity;
    private BigDecimal price;
    private ProductVariantStatus status;
    private MultipartFile featureImage;
    private String featureImageUrl;
    private MultipartFile[] images;
    private List<String> imagesUrl;
    private String description;

    public static ProductDetailRequest toProductDetailRequestDTO(ProductDetail productDetail) {
        List<String> imagesUrl = productDetail.getImages().stream().map(Image::getUrl).toList();
        return ProductDetailRequest.builder()
                .id(productDetail.getId())
                .productId(productDetail.getProduct().getId())
                .brandId(productDetail.getBrand().getId())
                .colorId(productDetail.getColor().getId())
                .materialId(productDetail.getMaterial().getId())
                .sizeId(productDetail.getSize().getId())
                .gender(productDetail.getGender())
                .quantity(productDetail.getQuantity())
                .price(productDetail.getPrice())
                .status(productDetail.getStatus())
                .featureImageUrl(productDetail.getFeatureImage())
                .imagesUrl(imagesUrl)
                .description(productDetail.getDescription())
                .build();
    }

}
