package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailFilterRequest;
import org.fpoly.capstone.service.payload.product_detail.ProductDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            "WHERE (:#{#request.categoryId} IS NULL OR p.category.id = :#{#request.categoryId}) " +
            "AND (:#{#request.productId} IS NULL OR pd.product.id = :#{#request.productId}) " +
            "AND (:#{#request.brandId} IS NULL OR pd.brand.id = :#{#request.brandId}) " +
            "AND (:#{#request.colorId} IS NULL OR pd.color.id = :#{#request.colorId}) " +
            "AND (:#{#request.materialId} IS NULL OR pd.material.id = :#{#request.materialId}) " +
            "AND (:#{#request.sizeId} IS NULL OR pd.size.id = :#{#request.sizeId}) " +
            "AND (:#{#request.gender} IS NULL OR pd.gender = :#{#request.gender}) " +
            "AND (:#{#request.status} IS NULL OR pd.status = :#{#request.status})")
    Page<ProductDetailResponse> findByFilter(ProductDetailFilterRequest request, Pageable pageable);

    @Query("SELECT i.url FROM Image i WHERE i.productDetail.id = :productDetailId")
    List<String> findImagesByProductDetailId(@Param("productDetailId") Long productDetailId);


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
