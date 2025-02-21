package org.fpoly.capstone.controller.payload.product_detail;

import lombok.Getter;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;

@Getter
@Setter
public class ProductDetailFilterModel {

    private Long categoryId;
    private Long productId;
    private Long brandId;
    private Long colorId;
    private Long materialId;
    private Long sizeId;
    private Gender gender;
    private Integer quantity;
    private ProductVariantStatus status;

}
