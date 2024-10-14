package org.fpoly.capstone.service.payload.product;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

  private Integer id;

  @NotBlank(message = "Code is required")
  private String code;

  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Weight is required")
  private String weight;

  @NotBlank(message = "GSM Qualification is required")
  private String gsmQualification;
  
  private String featureImageUrl;
  private MultipartFile featureImage;
  private String description;
  private String sizeGuideUrl;
  private MultipartFile sizeGuideImage;
  private Boolean status;

}
