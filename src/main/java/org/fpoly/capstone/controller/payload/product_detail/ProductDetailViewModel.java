package org.fpoly.capstone.controller.payload.product_detail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.enum_status.Gender;
import org.fpoly.capstone.entity.enum_status.ProductVariantStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailViewModel {

    private Long id;
    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;
    private Long sizeId;
    private String sizeName;
    private Long colorId;
    private String colorName;
    private Long materialId;
    private String materialName;
    private Gender gender;
    private Integer quantity;
    private BigDecimal price;
    private ProductVariantStatus status;
    private String description;
    private String featureImageUrl;
    private List<String> imagesUrl;

}
