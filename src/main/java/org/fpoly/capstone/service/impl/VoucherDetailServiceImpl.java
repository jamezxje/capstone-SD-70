package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.exceptions.NotException;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.repository.VoucherDetailRepository;
import org.fpoly.capstone.repository.VoucherRepository;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.VoucherDetailService;
import org.fpoly.capstone.service.VoucherService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import org.fpoly.capstone.dto.voucherDetail.VoucherDetailDTO;
import org.fpoly.capstone.repository.VoucherDetailRepository;
import org.fpoly.capstone.service.VoucherDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoucherDetailServiceImpl implements VoucherDetailService {

    private final VoucherDetailRepository detailRepository;
    private final VoucherService voucherService;
    private final UserService userService;

    private final BillService billService;

    @Override
    public List<VoucherDetail> findAll() {
        return detailRepository.findAll();
    }

    @Override
    public VoucherDetail create(Long voucherId, Long billId, VoucherDetail voucherDetail) throws NotException {
        Voucher voucher = voucherService.findById(voucherId);
        Bill bill = billService.findById(billId);

        if (voucher == null || bill == null) {
            throw new IllegalArgumentException("Voucher or Bill not found");
        }

        String user = userService.getName();

        VoucherDetail newVoucherDetail = new VoucherDetail();
        newVoucherDetail.setVoucher(voucher);
        newVoucherDetail.setBill(bill);
        newVoucherDetail.setAfterPrice(voucherDetail.getAfterPrice());
        newVoucherDetail.setBeforePrice(voucherDetail.getBeforePrice());
        newVoucherDetail.setDiscountPrice(voucherDetail.getDiscountPrice());
        newVoucherDetail.setCreateDate(new Date());
        newVoucherDetail.setCreatedBy(user);
        newVoucherDetail.setUpdatedBy(user);



        log.info("(create) :" + newVoucherDetail);
        return detailRepository.save(newVoucherDetail);
    }

    @Override
    public VoucherDetail update(Long voucherId, Long billId, VoucherDetail voucherDetail) throws NotException {
        Voucher voucher = voucherService.findById(voucherId);
        Bill bill = billService.findById(billId);
        String user = userService.getName();

        VoucherDetail newVoucherDetail = findById(voucherDetail.getId());
        newVoucherDetail.setVoucher(voucher);
        newVoucherDetail.setBill(bill);
        newVoucherDetail.setAfterPrice(voucherDetail.getAfterPrice());
        newVoucherDetail.setBeforePrice(voucherDetail.getBeforePrice());
        newVoucherDetail.setDiscountPrice(voucherDetail.getDiscountPrice());
        newVoucherDetail.setUpdatedBy(user);


        log.info("(update) :" + newVoucherDetail);
        return detailRepository.save(newVoucherDetail);
    }

    @Override
    public VoucherDetail delete(VoucherDetail voucherDetail) {
        return null;
    }

    @Override
    public VoucherDetail findById(Long id) {
        VoucherDetail detail = detailRepository.findById(id)
                .orElseThrow();
        return detail;
    }
    @Override
    public Optional<VoucherDetailDTO> getVoucherDetailsByBillId(Long billId) {
        List<Object[]> results = detailRepository.findPricesByBillId(billId);
        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            BigDecimal beforePrice = (row[0] != null) ? new BigDecimal(row[0].toString()) : BigDecimal.ZERO;
            BigDecimal afterPrice = (row[1] != null) ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
            BigDecimal discountPrice = (row[2] != null) ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
            BigDecimal moneyShip = (row[3] != null) ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO;
            return Optional.of(new VoucherDetailDTO(beforePrice, afterPrice, discountPrice, moneyShip));
        }
        return Optional.empty();
    }
}


