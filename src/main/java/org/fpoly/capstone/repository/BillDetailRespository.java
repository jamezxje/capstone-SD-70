package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRespository extends JpaRepository<BillDetail, Long> {
}
