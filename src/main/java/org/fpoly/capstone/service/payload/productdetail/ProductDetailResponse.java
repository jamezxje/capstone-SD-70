package org.fpoly.capstone.service.payload.productdetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {

  private Integer productId;
  private String productName;
  private String productWeight;
  private String gsmQualification;
  private String categoryName;
  private String colorName;
  private String materialName;
  private String sizeName;
  private BigDecimal basePrice;
  private Long stockQuantity;

}
