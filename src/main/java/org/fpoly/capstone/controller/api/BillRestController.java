package org.fpoly.capstone.controller.api;

import org.fpoly.capstone.dto.billDetail.ChangeStatusBillRequest;
import org.fpoly.capstone.dto.billDetail.StatusBillDetailRequest;
import org.fpoly.capstone.dto.billDetail.UpdateInForCustomer;
import org.fpoly.capstone.dto.voucherdetail.VoucherPriceDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BillRestController {
    @Autowired
    private BillDetailService billDetaiService;

    @PutMapping("/change-status/{id}")
    public Bill changeBillStatus(
            @PathVariable Long id,
            @RequestParam Long idEmployee,
            @RequestBody ChangeStatusBillRequest request) {

        return billDetaiService.changeStatusBill(id, idEmployee, request);
    }

    @GetMapping("/getStatus-history")
    public List<StatusBillDetailRequest> getStatusHistory(@Param("id") Long id) {
        return billDetaiService.getStatusBillHistory(id);
    }
    @GetMapping("/getStatus-history-customer")
    public List<StatusBillDetailRequest> getStatusHistoryCustomer(@Param("code") String code) {
        return billDetaiService.getStatusBillHistoryCustomer(code);
    }

    @GetMapping("/getInforBill/{id}")
    public Bill getInforBill(@PathVariable Long id) {
        return billDetaiService.getInforBillId(id);
    }

    @GetMapping("/getInforBillCustomer/{code}")
    public Bill getInforBillCustomer(@PathVariable String code) {
        return billDetaiService.getInForBillCustomer(code);
    }

    @PostMapping("/updateCustomer-bill/{id}")
    public Bill updateInforBill(@RequestBody UpdateInForCustomer request , @PathVariable Long id) {
        return billDetaiService.updateInforBill(id , request);
    }
    @PutMapping("/cancel-bill/{id}")
    public Bill cancelBill(@PathVariable Long id , @RequestParam Long idEmployee , @RequestBody ChangeStatusBillRequest request) {
        return billDetaiService.cancelBillAdmin(id, idEmployee, request);
    }
    @PutMapping("/cancel-bill-customer/{code}")
    public Bill cancelBillCustomer(@PathVariable String code , @RequestParam Long idEmployee , @RequestBody ChangeStatusBillRequest request) {
        return billDetaiService.cancelBillCustomer(code, idEmployee, request);
    }
    @GetMapping("/getTotalBill/{code}")
    public ResponseEntity<?> getPricesByBillId(@PathVariable String code) {
        try {
            List<VoucherPriceDTO> prices = billDetaiService.getVoucherDetail(code);

            if (prices.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin giá cho Bill ID: " + code);
            }
            return ResponseEntity.ok(prices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
        }
    }
}
