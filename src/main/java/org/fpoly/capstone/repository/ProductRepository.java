package org.fpoly.capstone.repository;

import org.fpoly.capstone.dto.bill.ProductRequest;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.entity.Product;
import org.fpoly.capstone.service.payload.product.ProductFilterRequest;
import org.fpoly.capstone.service.payload.product.ProductResponse;
import org.fpoly.capstone.service.payload.product.ProductUserResponse;
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
            "JOIN p.productVariantList pd " +
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
            "JOIN p.productVariantList pd " +
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

    @Query(" SELECT new org.fpoly.capstone.service.payload.product.ProductResponse(p.id, p.code, p.name, p.status,p.category.id, p.category.name, p.createDate, p.createdBy, p.lastModifiedDate, p.updatedBy )" +
            "FROM Product p" +
            " WHERE (:#{#request.code} IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :#{#request.code}, '%')))" +
            " AND (:#{#request.name} IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :#{#request.name}, '%')))" +
            " AND (:#{#request.status} IS NULL OR p.status = :#{#request.status})" +
            " AND (:#{#request.categoryId} IS NULL OR p.category.id = :#{#request.categoryId})")
    Page<ProductResponse> findByFilter(ProductFilterRequest request, Pageable pageable);

    @Query("SELECT p FROM Product  p WHERE p.status = org.fpoly.capstone.entity.enum_status.ProductStatus.DANG_SU_DUNG")
    List<Product> findAllActiveProduct();

    @Query("SELECT new org.fpoly.capstone.service.payload.product.ProductUserResponse(" +
            "    p.id ," +
            "    pd.id ," +
            "    p.code," +
            "    p.name," +
            "    p.status," +
            "    p.category.name," +
            "    pd.brand.name," +
            "    pd.color.name," +
            "    pd.material.name," +
            "    pd.gender," +
            "    pd.price," +
            "    pd.description," +
            "    pd.featureImage)" +
            "FROM Product p " +
            "JOIN ProductDetail pd ON p.id = pd.product.id")
    List<ProductUserResponse> getProductForOnlineUser();


}
