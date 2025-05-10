package org.fpoly.capstone.service.payload.cart_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailResponse {

    private Long id;
    private Long productDetailId;
    private String productName;
    private String productFeatureImageUrl;
    private String sizeName;
    private String colorName;
    private BigDecimal price;
    private Integer quantity;

}
