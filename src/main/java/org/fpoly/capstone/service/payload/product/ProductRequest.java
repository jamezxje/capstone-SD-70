package org.fpoly.capstone.service.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private String code;
    private List<ProductDetailRequest> variants;

    @Getter
    @Setter
    public static class ProductDetailRequest {
        private Long productId;
        private Long categoryId;
        private Long brandId;
        private Long colorId;
        private Long materialId;
        private Long sizeId;
        private Long stockQuantity;
        private Double basePrice;
        private Boolean status;
        private MultipartFile featureImage;
        private String featureImageURL;
        private MultipartFile[] images;
        private List<String> imageURLs;
        private String description;
    }

}
