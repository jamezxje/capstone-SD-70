package org.fpoly.capstone.controller.payload.product_detail;

import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDetailModel {

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
    private String description;

}
