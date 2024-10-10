package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Category;
import org.fpoly.capstone.service.payload.category.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("SELECT new org.fpoly.capstone.service.payload.category.CategoryResponse(c.id, c.name, c.status, c.createdOn, c.updatedOn) " +
      "FROM Category c " +
      "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
      "AND (:status IS NULL OR c.status = :status)")
  Page<CategoryResponse> findByFilter(@Param("name") String name, @Param("status") Boolean status, Pageable pageable);

}
