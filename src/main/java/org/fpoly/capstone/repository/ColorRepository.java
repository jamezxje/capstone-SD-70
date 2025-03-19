package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Color;
import org.fpoly.capstone.service.payload.color.ColorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findAll();

    @Query("SELECT new org.fpoly.capstone.service.payload.color.ColorResponse(c.id, c.name) " +
            "FROM Color c " +
            "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) ")
    Page<ColorResponse> findByFilter(@Param("name") String name, Pageable pageable);

    @Query("SELECT pd.color FROM ProductDetail pd where pd.product.id = :productId")
    List<Color> findSizesByProductId(@Param("productId") Long productId);

}
