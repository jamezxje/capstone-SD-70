package org.fpoly.capstone.controller;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.CustomerService;
import org.fpoly.capstone.service.ProductDetailService;
import org.fpoly.capstone.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BillController {

        @GetMapping("/sale-counter")
    public String saleCounter( ) {
        return "views/saleCounter/sale";
    }
@GetMapping("/payment-success")
    public String paymnetSuccess() {
            return "views/saleCounter/PayMentVNPAYSuccess";
}
}
