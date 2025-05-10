package org.fpoly.capstone.service.payload.product_detail;

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
public class ProductDetailResponse {

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

    public ProductDetailResponse(Long id, Long productId, String productName, Long categoryId, String categoryName, Long brandId, String brandName, Long sizeId, String sizeName, Long colorId, String colorName, Long materialId, String materialName, Gender gender, Integer quantity, BigDecimal price, ProductVariantStatus status, String description, String featureImageUrl) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.brandId = brandId;
        this.brandName = brandName;
        this.sizeId = sizeId;
        this.sizeName = sizeName;
        this.colorId = colorId;
        this.colorName = colorName;
        this.materialId = materialId;
        this.materialName = materialName;
        this.gender = gender;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.description = description;
        this.featureImageUrl = featureImageUrl;
    }

}
