package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
  @Query("SELECT new org.fpoly.capstone.service.payload.productdetail.ProductDetailResponse(pd.product.id, pd.product.name, pd.product.weight, pd.product.gsmQualification, " +
      "pd.category.name, pd.color.name, pd.material.name, pd.size.name, pd.basePrice, pd.stockQuantity) " +
      "FROM ProductDetail pd " +
      "WHERE (:name IS NULL OR LOWER(pd.product.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
      "AND (:weight IS NULL OR pd.product.weight = :weight) " +
      "AND (:gsmQualification IS NULL OR pd.product.gsmQualification = :gsmQualification) " +
      "AND (:categoryName IS NULL OR LOWER(pd.category.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) " +
      "AND (:colorName IS NULL OR LOWER(pd.color.name) LIKE LOWER(CONCAT('%', :colorName, '%'))) " +
      "AND (:materialName IS NULL OR LOWER(pd.material.name) LIKE LOWER(CONCAT('%', :materialName, '%'))) " +
      "AND (:sizeName IS NULL OR LOWER(pd.size.name) LIKE LOWER(CONCAT('%', :sizeName, '%')))")
  Page<ProductDetailResponse> findByFilter(
      @Param("name") String name,
      @Param("weight") String weight,
      @Param("gsmQualification") String gsmQualification,
      @Param("categoryName") String categoryName,
      @Param("colorName") String colorName,
      @Param("materialName") String materialName,
      @Param("sizeName") String sizeName,
      Pageable pageable
  );
}
