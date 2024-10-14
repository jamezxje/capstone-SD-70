package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT new org.fpoly.capstone.service.payload.product.ProductResponse(p.id, p.code, p.name, p.weight, p.gsmQualification, p.featureImage.url, " +
      "p.description, p.sizeGuide.url, p.status, p.createdOn, p.updatedOn) " +
      "FROM Product p " +
      "WHERE (:code IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%'))) " +
      "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
      "AND (:weight IS NULL OR LOWER(p.weight) LIKE LOWER(CONCAT('%', :weight, '%'))) " +
      "AND (:gsmQualification IS NULL OR LOWER(p.gsmQualification) LIKE LOWER(CONCAT('%', :gsmQualification, '%'))) " +
      "AND (:status IS NULL OR p.status = :status)")
  Page<ProductResponse> findByFilter(@Param("code") String code,
                                     @Param("name") String name,
                                     @Param("weight") String weight,
                                     @Param("gsmQualification") String gsmQualification,
                                     @Param("status") Boolean status,
                                     Pageable pageable);

  @Query("SELECT new org.fpoly.capstone.service.payload.product.ProductResponse(p.id, p.code, p.name, p.weight, p.gsmQualification, p.featureImage.url, " +
      "p.description, p.sizeGuide.url, p.status, p.createdOn, p.updatedOn) " +
      "FROM Product p ")
  Page<ProductResponse> findAllResponseProduct(Pageable pageable);

  Optional<Product> findByCode(String code);

}
