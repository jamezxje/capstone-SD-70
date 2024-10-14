package org.fpoly.capstone.service.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {

  private String code;
  private String name;
  private String weight;
  private String gsmQualification;
  private Boolean status;
  
}
