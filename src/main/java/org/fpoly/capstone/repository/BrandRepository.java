package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Brand;
import org.fpoly.capstone.service.payload.brand.BrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT new org.fpoly.capstone.service.payload.brand.BrandResponse(b.id, b.name, b.status, b.createdOn, b.createdBy, b.updatedOn, b.updatedBy) " +
            "FROM Brand b " +
            "WHERE (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:status IS NULL OR b.status = :status)")
    Page<BrandResponse> findByFilter(@Param("name") String name, @Param("status") Boolean status, Pageable pageable);

}
