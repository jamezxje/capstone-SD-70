package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Material;
import org.fpoly.capstone.service.payload.material.MaterialResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    @Query("SELECT new org.fpoly.capstone.service.payload.material.MaterialResponse(m.id, m.name, m.status, m.createdOn, m.createdBy, m.updatedOn, m.updatedBy) " +
            "FROM Material m " +
            "WHERE (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:status IS NULL OR m.status = :status)")
    Page<MaterialResponse> findByFilter(@Param("name") String name, @Param("status") Boolean status, Pageable pageable);

}
