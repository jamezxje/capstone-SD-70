package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.service.payload.product.ProductFilterRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(" SELECT new org.fpoly.capstone.service.payload.product.ProductResponse(p.id, p.code, p.name, p.status,p.category.id, p.category.name, p.createDate, p.createdBy, p.lastModifiedDate, p.updatedBy )" +
            "FROM Product p" +
            " WHERE (:#{#request.code} IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%')))" +
            " AND (:#{#request.name} IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%')))" +
            " AND (:#{#request.status} IS NULL OR p.status = :#{#request.status})" +
            " AND (:#{#request.categoryId} IS NULL OR p.category.id = :#{#request.categoryId})")
    Page<ProductResponse> findByFilter(ProductFilterRequest request, Pageable pageable);

    @Query("SELECT p FROM Product  p WHERE p.status = org.fpoly.capstone.entity.enum_status.ProductStatus.DANG_SU_DUNG")
    List<Product> findAllActiveProduct();

}
