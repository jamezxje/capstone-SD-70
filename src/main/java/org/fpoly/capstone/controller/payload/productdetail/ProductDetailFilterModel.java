package org.fpoly.capstone.controller.payload.productdetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailFilterModel {

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
