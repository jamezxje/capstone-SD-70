package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import lombok.Data;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        voucherService.updateVoucherStatuses();
        model.addAttribute("voucherPage", voucherPage);
        model.addAttribute("currentPage", (numPage == null || numPage <= 0) ? 1 : numPage);
        model.addAttribute("totalPages", voucherPage.getTotalPages() > 0 ? voucherPage.getTotalPages() : 1);
        model.addAttribute("status", VoucherStatus.values());

        return "views/voucher/listVoucher";
    }

    @PostMapping("/create")
    public String createVoucher(@ModelAttribute("voucher") Voucher voucher) throws Exception {
        voucherService.createVoucher(voucher);
        return "redirect:/dashboard/product-management/voucher/list";
    }


    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) throws NotException {
        Voucher voucher = voucherService.findById(id);

        DecimalFormat formatter = new DecimalFormat("###,###.##");
        String formattedValue = formatter.format(voucher.getValue());

        Integer minimumBill = voucher.getMinimumBill();
        String formatted = "";

        if (minimumBill != null) {
            NumberFormat formatterMiniBill = NumberFormat.getInstance(new Locale("vi", "VN"));
            formatted = formatterMiniBill.format(minimumBill) + " VND";
        } else {
            formatted = "0 VND"; // hoặc "Không có"
        }

        model.addAttribute("status", VoucherStatus.values());
        model.addAttribute("voucher", voucher);
        model.addAttribute("startDate", voucher.getStartDate());
        model.addAttribute("endDate", voucher.getEndDate());
        model.addAttribute("formattedValue", formattedValue);
        model.addAttribute("miniBill", formatted);

        return "views/voucher/updateVoucher";
    }

    @GetMapping("/detail/{id}")
    public String detailById(@PathVariable("id") Long id, Model model) throws NotException {
        Voucher voucher = voucherService.findById(id);

        DecimalFormat formatter = new DecimalFormat("###,###.##");
        String formattedValue = formatter.format(voucher.getValue());

        Integer minimumBill = voucher.getMinimumBill();
        String formatted = "";

        if (minimumBill != null) {
            NumberFormat formatterMiniBill = NumberFormat.getInstance(new Locale("vi", "VN"));
            formatted = formatterMiniBill.format(minimumBill) + " VND";
        } else {
            formatted = "0 VND"; // hoặc "Không có"
        }

        model.addAttribute("status", VoucherStatus.values());
        model.addAttribute("voucher", voucher);
        model.addAttribute("startDate", voucher.getStartDate());
        model.addAttribute("endDate", voucher.getEndDate());
        model.addAttribute("formattedValue", formattedValue);
        model.addAttribute("miniBill", formatted);


        return "views/voucher/detailVoucher";
    }


    @PostMapping("/update")
    public String update(@ModelAttribute("voucher") Voucher voucher,
                         @RequestParam("status") VoucherStatus voucherStatus,
                         @RequestParam("startDate") LocalDateTime startDate,
                         @RequestParam("endDate") LocalDateTime endDate,
                         @RequestParam("value") String value,
                         @RequestParam("miniBill") String miniBill
    ) throws NotException {

        log.info("(update): " + startDate + endDate);
        BigDecimal bigDecimal = new BigDecimal(value);
        Integer miniBills = Integer.parseInt(miniBill);
        LocalDateTime now = LocalDateTime.now();
        voucherService.updateVoucher(voucher, voucherStatus, startDate, endDate, bigDecimal, miniBills);
        return "redirect:/dashboard/product-management/voucher/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer numPage,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "statusSelect", required = false) VoucherStatus statusSelect,
                         @RequestParam(name = "startDate", required = false) LocalDate startDate,
                         @RequestParam(name = "endDate", required = false) LocalDate endDate,
                         Model model) throws NotException {
        Integer size = 5;
        Pageable pageable = PageRequest.of(numPage-1,size);
        Page<Voucher> voucherPage = Page.empty();

        if (name != null && name.trim().isEmpty()) {
            name = null;
        }

        if(name != null || statusSelect != null){
            voucherPage = voucherService.searchNameOrStatus(pageable, name, statusSelect);
        } else if (startDate != null && endDate != null) {
            voucherPage = voucherService.searchByStartDateAndEndDate(pageable, null, null, startDate, endDate);
        }
        if(voucherPage.isEmpty()){
            voucherPage = voucherService.findAll(pageable);
        }
        voucherService.updateVoucherStatuses();

        log.info("(startDate) " + startDate);
        log.info("(endDate) " + endDate);
        log.info("(name) " + name);
        log.info("(status) " + statusSelect);
        log.info("(search)" + voucherPage);

        model.addAttribute("currentPage", (numPage == null || numPage <= 0) ? 1 : numPage);
        model.addAttribute("totalPages", voucherPage.getTotalPages() > 0 ? voucherPage.getTotalPages() : 1);
        model.addAttribute("statusSelect", statusSelect);
        model.addAttribute("status", VoucherStatus.values());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("name", name);
        model.addAttribute("voucherPage", voucherPage);
        return "views/voucher/listVoucher";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) throws NotException {
        voucherService.deleteVoucher(id);
        return "redirect:/dashboard/product-management/voucher/list";
    }

}
