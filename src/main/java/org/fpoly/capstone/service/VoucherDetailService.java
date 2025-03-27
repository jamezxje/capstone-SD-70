package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.voucherDetail.VoucherDetailDTO;

import java.util.Optional;

public interface VoucherDetailService {
    Optional<VoucherDetailDTO> getVoucherDetailsByBillId(Long billId);
}
