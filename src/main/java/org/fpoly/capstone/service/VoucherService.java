package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.fpoly.capstone.exceptions.NotException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher) throws Exception;

    Page<Voucher> findAll(Pageable pageable);

    Voucher findById(Long id) throws NotException;

    List<Voucher> findAll();

    Voucher updateVoucher(Voucher voucher, VoucherStatus voucherStatus,
                          LocalDateTime startDate, LocalDateTime endDate, BigDecimal value, Integer miniBill) throws NotException;

    Voucher findByCode(String code) throws Exception;

    Voucher deleteVoucher(Long voucherId) throws NotException;

    List<Long> findAllById();

    Page<Voucher> searchByStartDateAndEndDate(Pageable pageable, String name, VoucherStatus status, LocalDate startOfDay, LocalDate endOfDay);

    Page<Voucher> searchNameOrStatus(Pageable pageable, String name, VoucherStatus status);

    Page<Voucher> searchByCreateAt(Pageable pageable, String name, VoucherStatus status, LocalDate date);



}
