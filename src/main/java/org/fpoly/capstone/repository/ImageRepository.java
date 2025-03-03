package org.fpoly.capstone.repository;

import jakarta.transaction.Transactional;
import org.fpoly.capstone.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    boolean existsByName(String fileName);

    @Query("SELECT i.url FROM Image i WHERE(:productDetailId) IS NULL OR i.productDetail.id = :productDetailId ")
    List<String> findImagesUrlByProductDetailId(@Param("productDetailId") Long productDetailId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE(:productDetailId) IS NULL OR i.productDetail.id = :productDetailId ")
    void deleteImagesByProductDetailId(@Param("productDetailId") Long productDetailId);

}
