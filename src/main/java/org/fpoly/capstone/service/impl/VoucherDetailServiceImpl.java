package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.dto.voucherDetail.VoucherDetailDTO;
import org.fpoly.capstone.repository.VoucherDetailRepository;
import org.fpoly.capstone.service.VoucherDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherDetailServiceImpl implements VoucherDetailService {

    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    @Override
    public Optional<VoucherDetailDTO> getVoucherDetailsByBillId(Long billId) {
        List<Object[]> results = voucherDetailRepository.findPricesByBillId(billId);
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

