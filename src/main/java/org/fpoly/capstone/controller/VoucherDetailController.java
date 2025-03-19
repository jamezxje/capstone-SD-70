package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.exceptions.NotException;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.VoucherDetailService;
import org.fpoly.capstone.service.VoucherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard/product-management/voucher-detail")
@RequiredArgsConstructor
@Slf4j
public class VoucherDetailController {

    private final VoucherService voucherService;
    private final BillService billService;
    private final VoucherDetailService voucherDetailService;

    @GetMapping("/list")
    public String listVoucherDT(Model model){
        List<VoucherDetail> voucherDetails = voucherDetailService.findAll();
        model.addAttribute("voucherDetails", voucherDetails);

        return "views/voucher-detail/list";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("voucherDetail", new VoucherDetail());
        model.addAttribute("voucher", voucherService.findAllById());
        model.addAttribute("bill", billService.findAllById());

        return "views/voucher-detail/create";
    }


    @PostMapping("/create")
    public String createVoucherDT(@Valid @ModelAttribute VoucherDetail voucherDetail,
                                  @RequestParam("voucherId") Long voucherId,
                                  @RequestParam("billId") Long billId
                                    , BindingResult result) throws NotException {
        if (result.hasErrors()) {
            return "views/voucher-detail/create";
        }
        voucherDetailService.create(voucherId, billId, voucherDetail);
        return "redirect:/dashboard";
    }



    @GetMapping("/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        VoucherDetail list = voucherDetailService.findById(id);
        model.addAttribute("list", list);
        model.addAttribute("voucher", voucherService.findAllById());
        model.addAttribute("bill", billService.findAllById());


        return "views/voucher-detail/update";
    }

    @PostMapping("/update")
    public String updateVoucherDT(@RequestParam("id") Long id,
                                  @RequestParam("voucherId") Long voucherId,
                                  @RequestParam("billId") Long billId) throws NotException {
        VoucherDetail detail = voucherDetailService.findById(id);
        log.info("(update) :" +id + voucherId + billId);
        System.out.println("Tao that bai");
        voucherDetailService.update(voucherId, billId, detail);
        return "redirect:/dashboard";
    }



}
