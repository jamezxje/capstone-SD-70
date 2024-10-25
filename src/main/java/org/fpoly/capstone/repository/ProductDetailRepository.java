package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.payload.productdetail.ProductDetailFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

  @Query("SELECT pd FROM ProductDetail pd " +
      "LEFT JOIN FETCH pd.product p " +
      "LEFT JOIN FETCH pd.category c " +
      "LEFT JOIN FETCH pd.color co " +
      "LEFT JOIN FETCH pd.material m " +
      "LEFT JOIN FETCH pd.size s " +
      "LEFT JOIN FETCH pd.images i " +
      "WHERE (:#{#request.code} IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%'))) " +
      "AND (:#{#request.name} IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%'))) " +
      "AND (:#{#request.gsmQualification} IS NULL OR LOWER(p.gsmQualification) LIKE LOWER(CONCAT('%', :#{#request.gsmQualification}, '%'))) " +
      "AND (:#{#request.productId} IS NULL OR p.id = :#{#request.productId}) " +
      "AND (:#{#request.categoryId} IS NULL OR c.id = :#{#request.categoryId}) " +
      "AND (:#{#request.colorId} IS NULL OR co.id = :#{#request.colorId}) " +
      "AND (:#{#request.materialId} IS NULL OR m.id = :#{#request.materialId}) " +
      "AND (:#{#request.sizeId} IS NULL OR s.id = :#{#request.sizeId}) " +
      "AND (:#{#request.status} IS NULL OR pd.status = :#{#request.status})")
  Page<ProductDetail> findByFilter(ProductDetailFilterRequest request, Pageable pageable);

}
