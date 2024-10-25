package org.fpoly.capstone.controller.payload.productdetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.entity.Image;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailViewModel {

  private Integer productDetailId;
  private Integer productId;
  private String productCode;
  private String productName;
  private String productWeight;
  private String gsmQualification;
  private String featureImageUrl;
  private String sizeGuideUrl;
  private String description;
  private Integer categoryId;
  private String categoryName;
  private Integer colorId;
  private String colorName;
  private Integer materialId;
  private String materialName;
  private Integer sizeId;
  private String sizeName;
  private Double basePrice;
  private Long stockQuantity;
  private List<Image> images;
  private List<String> imageUrls;
  private Boolean status;

}
