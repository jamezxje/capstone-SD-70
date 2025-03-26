package org.fpoly.capstone.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Long id;
    private String code;
    private String name;
    private String categoryName;
    private String sizeName;
    private String colorName;
    private String materialName;
    private String brandName;
    private Integer quantity;
    private BigDecimal price;
    private Gender gender;
    private ProductVariantStatus status;
    private String image;
}
