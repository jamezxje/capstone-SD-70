package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.billDetail.ChangeStatusBillRequest;
import org.fpoly.capstone.dto.billDetail.StatusBillDetailRequest;
import org.fpoly.capstone.dto.billDetail.UpdateInForCustomer;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillHistory;

import java.util.List;

public interface BillDetaiService {
        Bill changeStatusBill(Long id , Long idEmployess , ChangeStatusBillRequest request);

        List<StatusBillDetailRequest> getStatusBillHistory(Long id);

        Bill getInforBillId(Long id);

        Bill updateInforBill(Long id , UpdateInForCustomer request);

        Bill cancelBillAdmin(Long id , Long idEmployess , ChangeStatusBillRequest request);
}
