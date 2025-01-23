package org.fpoly.capstone.service.payload.productdetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailRequest {

    private Long productId;
    private Long categoryId;
    private Long colorId;
    private Long materialId;
    private Long sizeId;
    private Long stockQuantity;
    private Double basePrice;
    private Boolean status;

}
