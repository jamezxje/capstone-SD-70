package org.fpoly.capstone.service.payload.productdetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailFilterRequest {

  private String code;
  private String name;
  private String gsmQualification;
  private Integer productId;
  private Integer categoryId;
  private Integer colorId;
  private Integer materialId;
  private Integer sizeId;
  private Boolean status;

}
