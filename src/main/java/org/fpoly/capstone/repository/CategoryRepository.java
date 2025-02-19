package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.service.payload.category.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new org.fpoly.capstone.service.payload.category.CategoryResponse(c.id, c.name, c.status) " +
            "FROM Category c " +
            "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) ")
    Page<CategoryResponse> findByFilter(@Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM Category  c WHERE c.status = org.fpoly.capstone.entity.enum_status.CategoryStatus.DANG_SU_DUNG")
    List<Category> findAllActiveCategory();

}
