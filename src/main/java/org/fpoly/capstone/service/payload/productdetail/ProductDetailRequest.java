package org.fpoly.capstone.service.payload.productdetail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductDetailRequest {

  private Integer id;
  private Integer productId;
  private Integer categoryId;
  private Integer colorId;
  private Integer materialId;
  private Integer sizeId;
  private Long stockQuantity;
  private Double basePrice;
  private Boolean status;
  private MultipartFile[] images;
  private List<String> imageUrls;

}
