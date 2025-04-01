package org.fpoly.capstone.service;

import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;

import java.util.List;

public interface BillDetailService {

    List<BillDetailResponse> findBillDetailByBillId(Long billId);


}
