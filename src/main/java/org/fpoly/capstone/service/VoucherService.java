package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.fpoly.capstone.exceptions.NotException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface VoucherService {
    List<Voucher> getAllVouchers();

    Voucher createVoucher(Voucher voucher, VoucherStatus voucherStatus);

    Page<Voucher> findAll(Pageable pageable);

    Voucher findById(Long id) throws NotException;

    List<Voucher> findAll();

    Voucher updateVoucher(Voucher voucher, VoucherStatus voucherStatus) throws NotException;

    Voucher deleteVoucher(Long voucherId) throws NotException;

    List<Long> findAllById();

    Page<Voucher> search(Pageable pageable, String name, VoucherStatus status);

}
