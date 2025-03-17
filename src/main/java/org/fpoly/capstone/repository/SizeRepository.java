package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.service.payload.size.SizeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

    @Query("SELECT new org.fpoly.capstone.service.payload.size.SizeResponse(s.id, s.name) " +
            "FROM Size s " +
            "WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) ")
    Page<SizeResponse> findByFilter(@Param("name") String name, Pageable pageable);

    @Query("SELECT pd.size FROM ProductDetail pd where pd.product.id = :productId")
    List<Size> findSizesByProductId(@Param("productId") Long productId);

    @Query("SELECT pd.size FROM ProductDetail pd where pd.product.id = :productId AND pd.color.id = :colorId AND pd.quantity > 0")
    List<Size> findSizesByProductIdAndColorId(@Param("productId") Long productId, @Param("colorId") Long colorId);

}
