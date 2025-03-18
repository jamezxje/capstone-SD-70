package org.fpoly.capstone.repository;

import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("select v from Voucher v order by v.id desc")
    Page<Voucher> findAll(Pageable pageable);

    @Query("SELECT v FROM Voucher v WHERE v.name = :name or v.status = :status")
    Page<Voucher> search(Pageable pageable, @Param("name") String name ,@Param("status") VoucherStatus status);

    @Query("select v.id from Voucher v")
    List<Long> findByAllIds();
}
