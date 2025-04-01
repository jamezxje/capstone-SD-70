package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.VoucherDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherDetailReponsitory extends JpaRepository<VoucherDetail, Long> {
}
