//package org.fpoly.capstone.service.payload.productdetail;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.fpoly.capstone.entity.Image;
//import org.fpoly.capstone.entity.ProductDetail;
//
//import java.util.List;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class ProductDetailResponse {
//
//  private Integer productDetailId;
//  private Integer productId;
//  private String productCode;
//  private String productName;
//  private String productWeight;
//  private String gsmQualification;
//  private String featureImageUrl;
//  private String sizeGuideUrl;
//  private String description;
//  private Integer categoryId;
//  private String categoryName;
//  private Integer colorId;
//  private String colorName;
//  private Integer materialId;
//  private String materialName;
//  private Integer sizeId;
//  private String sizeName;
//  private Double basePrice;
//  private Long stockQuantity;
//  private List<Image> images;
//  private List<String> imageUrls;
//  private Boolean status;
//
//  public static ProductDetailResponse toDTO(ProductDetail productDetail) {
//    List<String> imageUrls = productDetail.getImages().stream().map(Image::getUrl).toList();
//    return ProductDetailResponse.builder()
//        .productDetailId(productDetail.getId())
//        .productId(productDetail.getProduct().getId())
//        .productCode(productDetail.getProduct().getCode())
//        .productName(productDetail.getProduct().getName())
//        .productWeight(productDetail.getProduct().getWeight())
//        .gsmQualification(productDetail.getProduct().getGsmQualification())
//        .featureImageUrl(productDetail.getProduct().getFeatureImage().getUrl())
//        .sizeGuideUrl(productDetail.getProduct().getSizeGuide().getUrl())
//        .description(productDetail.getProduct().getDescription())
//        .categoryId(productDetail.getCategory().getId())
//        .categoryName(productDetail.getCategory().getName())
//        .colorId(productDetail.getColor().getId())
//        .colorName(productDetail.getColor().getName())
//        .materialId(productDetail.getMaterial().getId())
//        .materialName(productDetail.getMaterial().getName())
//        .sizeId(productDetail.getSize().getId())
//        .sizeName(productDetail.getSize().getName())
//        .basePrice(productDetail.getBasePrice())
//        .stockQuantity(productDetail.getStockQuantity())
//        .imageUrls(imageUrls)
//        .status(productDetail.getStatus())
//        .build();
//  }
//
//}
