package org.fpoly.capstone.controller;

import org.fpoly.capstone.dto.BillDetailDTO;
import org.fpoly.capstone.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import java.util.List;

@Controller
public class BillDetailController {
//    @Autowired
//    private BillDetailService billDetailService;
//
//    @GetMapping("/bill/{billId}/details")
//    public String getBillDetails(@PathVariable("billId") Long billId, Model model) {
//        List<BillDetailDTO> billDetails = billDetailService.getBillDetails(billId);
//        model.addAttribute("billDetailss", billDetails);
//        return "views/billDetail";
//    }
}
