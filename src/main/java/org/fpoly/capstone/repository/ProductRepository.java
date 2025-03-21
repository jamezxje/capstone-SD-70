package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.bill.ProductRequest;
import org.fpoly.capstone.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT pd.id, p.code, p.name, c.name AS categoryName, s.name AS sizeName, " +
            "cc.name AS colorName, m.name AS materialName, b.name AS brandName, " +
            "pd.quantity, pd.price, pd.gender, pd.status " +
            "FROM Product p " +
            "JOIN p.category c " +
            "JOIN p.productDetails pd " +
            "JOIN pd.size s " +
            "JOIN pd.color cc " +
            "JOIN pd.material m " +
            "JOIN pd.brand b  " +
            "WHERE pd.status = 'DANG_SU_DUNG'" +
            "ORDER BY pd.lastModifiedDate DESC")
    Page<Object[]> findAllProductDetails(Pageable pageable);

    @Query("SELECT pd.id, p.code, p.name, c.name AS categoryName, s.name AS sizeName, " +
            "cc.name AS colorName, m.name AS materialName, b.name AS brandName, " +
            "pd.quantity, pd.price, pd.gender, pd.status " +
            "FROM Product p " +
            "JOIN p.category c " +
            "JOIN p.productDetails pd " +
            "JOIN pd.size s " +
            "JOIN pd.color cc " +
            "JOIN pd.material m " +
            "JOIN pd.brand b " +
            "WHERE pd.status = 'DANG_SU_DUNG' " +
            "AND (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId) " +  // Tìm kiếm theo ID của category
            "AND (:colorId IS NULL OR cc.id = :colorId) " +  // Tìm kiếm theo ID của color
            "AND (:materialId IS NULL OR m.id = :materialId) " +  // Tìm kiếm theo ID của material
            "AND (:sizeId IS NULL OR s.id = :sizeId) " +  // Tìm kiếm theo ID của size
            "AND (:brandId IS NULL OR b.id = :brandId) " +  // Tìm kiếm theo ID của brand
            "ORDER BY pd.lastModifiedDate DESC")
    Page<Object[]> findAllProductDetailsBySearch(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,  // ID của category
            @Param("colorId") Long colorId,  // ID của color
            @Param("materialId") Long materialId,  // ID của material
            @Param("sizeId") Long sizeId,  // ID của size
            @Param("brandId") Long brandId,  // ID của brand
            Pageable pageable
    );


}
