package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {
    List<ProductDetail> findAll();

    @Query("SELECT new org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse(" +
            "pd.id, " +
            "pd.product.id, " +
            "pd.product.name, " +
            "pd.product.category.id, " +
            "pd.product.category.name, " +
            "pd.brand.id, " +
            "pd.brand.name, " +
            "pd.size.id, " +
            "pd.size.name, " +
            "pd.color.id, " +
            "pd.color.name, " +
            "pd.material.id, " +
            "pd.material.name, " +
            "pd.gender, " +
            "pd.quantity, " +
            "pd.price, " +
            "pd.status, " +
            "pd.description, " +
            "pd.featureImage) " +
            "FROM ProductDetail pd " +
            "LEFT JOIN pd.product p " +
            "WHERE (:productDetailId IS NULL OR pd.id = :productDetailId)")
    ProductDetailResponse findProductDetailById(@Param("productDetailId") Long productDetailId);

    @Query("SELECT new org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse(" +
            "pd.id, " +
            "pd.product.id, " +
            "pd.product.name, " +
            "pd.product.category.id, " +
            "pd.product.category.name, " +
            "pd.brand.id, " +
            "pd.brand.name, " +
            "pd.size.id, " +
            "pd.size.name, " +
            "pd.color.id, " +
            "pd.color.name, " +
            "pd.material.id, " +
            "pd.material.name, " +
            "pd.gender, " +
            "pd.quantity, " +
            "pd.price, " +
            "pd.status, " +
            "pd.description, " +
            "pd.featureImage) " +
            "FROM ProductDetail pd " +
            "LEFT JOIN pd.product p " +
            "WHERE (pd.status = org.fpoly.capstone.entity.enum_status.ProductVariantStatus.DANG_SU_DUNG)")
    List<ProductDetailResponse> findAllAvailableProductDetail();

    @Query("SELECT new org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse(" +
            "pd.id, " +
            "pd.product.id, " +
            "pd.product.name, " +
            "pd.product.category.id, " +
            "pd.product.category.name, " +
            "pd.brand.id, " +
            "pd.brand.name, " +
            "pd.size.id, " +
            "pd.size.name, " +
            "pd.color.id, " +
            "pd.color.name, " +
            "pd.material.id, " +
            "pd.material.name, " +
            "pd.gender, " +
            "pd.quantity, " +
            "pd.price, " +
            "pd.status, " +
            "pd.description, " +
            "pd.featureImage) " +
            "FROM ProductDetail pd " +
            "LEFT JOIN pd.product p " +
            "WHERE (pd.status = org.fpoly.capstone.entity.enum_status.ProductVariantStatus.DANG_SU_DUNG) " +
            "AND pd.brand.id = :brandId AND pd.id NOT IN (:productDetailId) ")
    List<ProductDetailResponse> findRelatedProductDetail(@Param("productDetailId") Long productDetailId, @Param("brandId") Long brandId, Pageable pageable);

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.size.id = :sizeId AND pd.color.id = :colorId")
    ProductDetail findProductDetailByIdAndSizeAndColor(@Param("productId") Long productId, @Param("sizeId") Long sizeId,
                                                       @Param("colorId") Long colorId);

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId")
    List<ProductDetail> findByProductId(@Param("productId") Long productId);

}
