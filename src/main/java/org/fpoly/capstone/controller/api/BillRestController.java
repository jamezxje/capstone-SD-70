package org.fpoly.capstone.controller.api;

import org.fpoly.capstone.dto.billDetail.ChangeStatusBillRequest;
import org.fpoly.capstone.dto.billDetail.StatusBillDetailRequest;
import org.fpoly.capstone.dto.billDetail.UpdateInForCustomer;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.service.BillDetaiService;
import org.fpoly.capstone.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @GetMapping("/getInforBill/{id}")
    public Bill getInforBill(@PathVariable Long id) {
        return billDetaiService.getInforBillId(id);
    }

    @PostMapping("/updateCustomer-bill/{id}")
    public Bill updateInforBill(@RequestBody UpdateInForCustomer request , @PathVariable Long id) {
        return billDetaiService.updateInforBill(id , request);
    }
    @PutMapping("/cancel-bill/{id}")
    public Bill cancelBill(@PathVariable Long id , @RequestParam Long idEmployee , @RequestBody ChangeStatusBillRequest request) {
        return billDetaiService.cancelBillAdmin(id, idEmployee, request);
    }
}
