package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {

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

}
