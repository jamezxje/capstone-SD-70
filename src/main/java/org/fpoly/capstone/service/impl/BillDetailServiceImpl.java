package org.fpoly.capstone.service.impl;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.repository.BillDetailRespository;
import org.fpoly.capstone.service.BillDetailService;
import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillDetailServiceImpl implements BillDetailService {

    private final BillDetailRespository billDetailRespository;


    @Override
    public List<BillDetailResponse> findBillDetailByBillId(Long billId) {
        return this.billDetailRespository.findBillDetailByBillId(billId);
    }
}
