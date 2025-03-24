package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.entity.enum_status.VoucherStatus;
import org.fpoly.capstone.exceptions.NotException;
import org.fpoly.capstone.service.VoucherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard/product-management/voucher")
@RequiredArgsConstructor
@Slf4j
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping("/create")
    public String showVoucher(Model model){
        model.addAttribute("voucher", new Voucher());
        model.addAttribute("status", VoucherStatus.values());
        return "views/voucher/createVoucher";
    }

    @GetMapping("/list")
    public String listVoucher(@RequestParam(defaultValue = "1") Integer numPage,
                              Model model){
        Integer size = 5;
        Pageable pageable = PageRequest.of(numPage-1,size);
        Page<Voucher> voucherPage  = voucherService.findAll(pageable);
        model.addAttribute("voucherPage", voucherPage);
        model.addAttribute("currentPage", (numPage == null || numPage <= 0) ? 1 : numPage);
        model.addAttribute("totalPages", voucherPage.getTotalPages() > 0 ? voucherPage.getTotalPages() : 1);
        model.addAttribute("status", VoucherStatus.values());
        model.addAttribute("voucherPage", voucherPage);
        return "views/voucher/listVoucher";
    }

    @PostMapping("/create")
    public String createVoucher(@ModelAttribute("voucher") Voucher voucher){
        voucherService.createVoucher(voucher);
        return "redirect:/dashboard/product-management/voucher/list";
    }


    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) throws NotException {
        Voucher voucher = voucherService.findById(id);

        model.addAttribute("status", VoucherStatus.values());
        model.addAttribute("voucher", voucher);
        model.addAttribute("startDate", voucher.getStartDate());
        model.addAttribute("endDate", voucher.getEndDate());
        return "views/voucher/updateVoucher";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("voucher") Voucher voucher,
            @RequestParam("status") VoucherStatus voucherStatus,
                         @RequestParam("startDate") LocalDateTime startDate,
                         @RequestParam("endDate") LocalDateTime endDate
                         ) throws NotException {

        log.info("(update): " + startDate + endDate);
        voucherService.updateVoucher(voucher, voucherStatus, startDate, endDate);
        return "redirect:/dashboard/product-management/voucher/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer numPage,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "status", required = false) VoucherStatus status,
                         Model model) throws NotException {
        Integer size = 5;
        Pageable pageable = PageRequest.of(numPage-1,size);
        Page<Voucher> voucherPage = voucherService.search(pageable, name, status);
        if(voucherPage.isEmpty()){
            voucherPage = voucherService.findAll(pageable);
        }

        model.addAttribute("status", VoucherStatus.values());
        model.addAttribute("voucherPage", voucherPage);
        return "views/voucher/listVoucher";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) throws NotException {
        voucherService.deleteVoucher(id);
        return "redirect:/dashboard/product-management/voucher/list";
    }

}
