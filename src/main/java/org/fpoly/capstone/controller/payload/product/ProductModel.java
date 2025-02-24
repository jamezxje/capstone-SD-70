package org.fpoly.capstone.controller.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductStatus;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    private Long id;
    private String code;
    private String name;
    private ProductStatus status;
    private Long categoryId;
    private List<ProductRequest.ProductDetailRequest> productVariantList;

    @Getter
    @Setter
    public static class ProductDetailRequest {
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
    }
}
