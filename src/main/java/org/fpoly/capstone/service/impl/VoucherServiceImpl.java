package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.fpoly.capstone.exceptions.NotException;
import org.fpoly.capstone.repository.VoucherRepository;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final UserService userService;

    @Override
    public Voucher createVoucher(Voucher voucher) {
        String user = userService.getName();

        if(voucher.getStartDate().isAfter(voucher.getEndDate())){
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        String code = "VC" + String.format("%05d", new Random().nextInt(100000));  ;

        Voucher newVoucher = new Voucher();

        newVoucher.setCode(code);
        newVoucher.setName(voucher.getName());
        newVoucher.setValue(voucher.getValue());
        newVoucher.setQuantity(voucher.getQuantity());
        newVoucher.setStartDate(voucher.getStartDate());
        newVoucher.setEndDate(voucher.getEndDate());

        updateVoucherStatus(newVoucher);
        newVoucher.setCreateDate(new Date());
        newVoucher.setLastModifiedDate(LocalDateTime.now());
        newVoucher.setCreatedBy(user);
        newVoucher.setUpdatedBy(user);
        log.info("{createVoucher}: "+ voucher);
        return voucherRepository.save(newVoucher);
    }

    @Override
    public Page<Voucher> findAll(Pageable pageable) {
        Page<Voucher> list = voucherRepository.findAll(pageable);

        return list;
    }


    @Override
    public Voucher findById(Long id) throws NotException {
        Optional<Voucher> voucher = voucherRepository.findById(id);
        if(voucher.isPresent()){
            return voucher.get();
        }
        throw new NotException("Voucher not found with id");

    }

    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    public int getTotalPages(int pageSize) {
        long totalProducts = voucherRepository.count();
        return (int) Math.ceil((double) totalProducts / pageSize);
    }

    @Override
    public Voucher updateVoucher(Voucher voucher, VoucherStatus voucherStatus,
                                 LocalDateTime startDate, LocalDateTime endDate) throws NotException {

        Voucher update = findById(voucher.getId());


        update.setName(voucher.getName());
        update.setValue(voucher.getValue());
        update.setQuantity(voucher.getQuantity());
        update.setStartDate(startDate);
        update.setEndDate(endDate);
        update.setStatus(voucherStatus);
        update.setLastModifiedDate(LocalDateTime.now());
        return voucherRepository.save(update);
    }

    @Override
    public List<Long> findAllById() {
        return voucherRepository.findByAllIds();
    }

    @Override
    public Page<Voucher> search(Pageable pageable, String name, VoucherStatus status) {
        Page<Voucher> search = voucherRepository.search(pageable, name, status);
        return search;
    }

    @Override
    public Voucher deleteVoucher(Long voucherId) throws NotException {
        Voucher deleteVoucher = findById(voucherId);
        voucherRepository.delete(deleteVoucher);
        return deleteVoucher;
    }

    private void updateVoucherStatus(Voucher voucher){
        LocalDate currentDate = LocalDate.now();

        if(voucher.getEndDate().toLocalDate().isBefore(currentDate)){
            voucher.setStatus(VoucherStatus.EXPIRED);
        }else if (voucher.getStartDate().toLocalDate().isAfter(currentDate)) {
            voucher.setStatus(VoucherStatus.INACTIVE);
        } else {
            voucher.setStatus(VoucherStatus.ACTIVE);
        }
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateVoucherStatuses() {
        List<Voucher> vouchers = voucherRepository.findAll();

        for (Voucher voucher : vouchers) {
            updateVoucherStatus(voucher);
            voucherRepository.save(voucher);
        }
    }






}
