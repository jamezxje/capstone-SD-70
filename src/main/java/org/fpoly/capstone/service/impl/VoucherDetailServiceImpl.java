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
}
