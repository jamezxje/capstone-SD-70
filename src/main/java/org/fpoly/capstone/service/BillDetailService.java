package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.billDetail.BillDetailDTO;
import org.fpoly.capstone.dto.billDetail.ChangeStatusBillRequest;
import org.fpoly.capstone.dto.billDetail.StatusBillDetailRequest;
import org.fpoly.capstone.dto.billDetail.UpdateInForCustomer;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface BillDetailService {
    List<BillDetailDTO> getBillDetails(Long billId);

    Bill changeStatusBill(Long id , Long idEmployess , ChangeStatusBillRequest request);

    List<StatusBillDetailRequest> getStatusBillHistory(Long id);

    Bill getInforBillId(Long id);

    Bill updateInforBill(Long id , UpdateInForCustomer request);

    Bill cancelBillAdmin(Long id , Long idEmployess , ChangeStatusBillRequest request);

    List<BillDetailResponse> findBillDetailByBillId(Long billId);

    List<BillDetail> findByCreateDate(LocalDate date);

    List<BillDetail> findByCreateDateBetween(LocalDate start, LocalDate end);

    List<BillDetail> findAll();

}
