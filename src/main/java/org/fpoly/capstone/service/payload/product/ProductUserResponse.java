package org.fpoly.capstone.service.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductStatus;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUserResponse {

    private Long productId;
    private Long productDetailId;
    private String code;
    private String name;
    private ProductStatus status;
    private String categoryName;
    private String brandName;
    private String colorName;
    private String materialName;
    private Gender gender;
    private BigDecimal price;
    private String description;
    private String featureImageUrl;

}
