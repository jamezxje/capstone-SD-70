package org.fpoly.capstone.controller.payload.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fpoly.capstone.service.payload.product.ProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    private String name;
    private String code;
    private MultipartFile featureImage;
    private String featureImageURL;
    private MultipartFile[] images;
    private List<String> imageURLs;
    private String description;
    private List<ProductRequest.ProductDetailRequest> variants;

    @Getter
    @Setter
    public static class ProductDetailRequest {
        private Long productId;
        private Long categoryId;
        private Long colorId;
        private Long materialId;
        private Long sizeId;
        private Long stockQuantity;
        private Double basePrice;
        private Boolean status;
    }

}
