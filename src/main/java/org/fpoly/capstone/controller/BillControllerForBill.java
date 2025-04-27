package org.fpoly.capstone.controller;

import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class BillControllerForBill {
@Autowired
private BillService billService;
@Autowired
private BillRepository billRepository;
    @GetMapping("/sale-counter")
    public String saleCounter( ) {
        return "views/saleCounter/sale";
    }
    @GetMapping("/payment-success")
    public String paymnetSuccess() {
        return "views/saleCounter/PayMentVNPAYSuccess";
    }

    @GetMapping("/searchBill")
    public String searchBill() {
        return "views/searchBill-ForCustomer/search";
    }

    @GetMapping("/searchBillCode")
    public String searchBillByCode() {
        return "views/searchBill-ForCustomer/searchBill";
    }

}
