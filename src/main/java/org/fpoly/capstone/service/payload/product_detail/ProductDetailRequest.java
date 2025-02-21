package org.fpoly.capstone.service.payload.product_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

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
    private MultipartFile[] images;
    private String description;

}
