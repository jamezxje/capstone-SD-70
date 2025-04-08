package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.fpoly.capstone.exceptions.NotException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);

    Page<Voucher> findAll(Pageable pageable);

    Voucher findById(Long id) throws NotException;

    List<Voucher> findAll();

    Voucher updateVoucher(Voucher voucher, VoucherStatus voucherStatus,
                          LocalDateTime startDate, LocalDateTime endDate, BigDecimal value) throws NotException;

    Voucher deleteVoucher(Long voucherId) throws NotException;

    List<Long> findAllById();

    Page<Voucher> search(Pageable pageable, String name, VoucherStatus status);

}
