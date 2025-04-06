package org.fpoly.capstone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class BillControllerForBill {

    @GetMapping("/sale-counter")
    public String saleCounter( ) {
        return "views/saleCounter/sale";
    }
    @GetMapping("/payment-success")
    public String paymnetSuccess() {
        return "views/saleCounter/PayMentVNPAYSuccess";
    }
}
