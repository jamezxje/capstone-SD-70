package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.billDetail.BillDetailDTO;
import org.fpoly.capstone.dto.billDetail.ChangeStatusBillRequest;
import org.fpoly.capstone.dto.billDetail.StatusBillDetailRequest;
import org.fpoly.capstone.dto.billDetail.UpdateInForCustomer;
import org.fpoly.capstone.dto.voucherdetail.VoucherPriceDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;

import java.time.LocalDate;
import java.util.List;

public interface BillDetailService {
    List<BillDetailDTO> getBillDetails(Long billId);

    Bill changeStatusBill(Long id , Long idEmployess , ChangeStatusBillRequest request);

    List<StatusBillDetailRequest> getStatusBillHistory(Long id);

    List<StatusBillDetailRequest> getStatusBillHistoryCustomer(String code);

    Bill getInforBillId(Long id);

    Bill getInForBillCustomer(String code);

    Bill updateInforBill(Long id , UpdateInForCustomer request);

    Bill cancelBillAdmin(Long id , Long idEmployess , ChangeStatusBillRequest request);

    Bill cancelBillCustomer(String code , Long idCusomter , ChangeStatusBillRequest request);

    List<BillDetailResponse> findBillDetailByBillId(Long billId);

    List<BillDetail> findAll();

    List<BillDetail> findByCreateDate(LocalDate date);

    List<BillDetail> findByCreateDateBetween(LocalDate start, LocalDate end);

    List<VoucherPriceDTO> getVoucherDetail(String code);

}
