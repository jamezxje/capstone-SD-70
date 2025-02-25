package org.fpoly.capstone.service.payload.product_detail;

import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDetailFilterRequest {

    private Long categoryId;
    private String productName;
    private Long brandId;
    private Long colorId;
    private Long materialId;
    private Long sizeId;
    private Gender gender;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private ProductVariantStatus status;

}
