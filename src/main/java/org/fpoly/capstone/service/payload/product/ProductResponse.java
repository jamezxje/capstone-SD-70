package org.fpoly.capstone.service.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

  private Integer id;
  private String code;
  private String name;
  private String weight;
  private String gsmQualification;
  private String featureImageUrl;
  private String description;
  private String sizeGuideUrl;
  private Boolean status;
  private LocalDateTime createdOn;
  private LocalDateTime updatedOn;

}
