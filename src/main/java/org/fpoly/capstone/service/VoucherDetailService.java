package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.voucherDetail.VoucherDetailDTO;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.exceptions.NotException;

import java.util.List;
import java.util.Optional;

public interface VoucherDetailService {

    List<VoucherDetail> findAll();

    VoucherDetail create(Long voucherId, Long billId, VoucherDetail voucherDetail) throws NotException;

    VoucherDetail update(Long voucherId, Long billId, VoucherDetail voucherDetail) throws NotException;

    VoucherDetail delete(VoucherDetail voucherDetail);

    VoucherDetail findById(Long id);

    Optional<VoucherDetailDTO> getVoucherDetailsByBillId(Long billId);

}
