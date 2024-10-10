package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Size;
import org.fpoly.capstone.service.payload.size.SizeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

  @Query("SELECT new org.fpoly.capstone.service.payload.size.SizeResponse(s.id, s.name, s.status, s.createdOn, s.updatedOn) " +
      "FROM Size s " +
      "WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
      "AND (:status IS NULL OR s.status = :status)")
  Page<SizeResponse> findByFilter(@Param("name") String name, @Param("status") Boolean status, Pageable pageable);

}
