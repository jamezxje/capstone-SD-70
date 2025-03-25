package org.fpoly.capstone.controller;

import org.fpoly.capstone.dto.BillDetailDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.repository.*;
import org.fpoly.capstone.service.BillDetailService;
import org.fpoly.capstone.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private BillHistotyRepository billHistotyRepository;

    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    @GetMapping
    public String listBills(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String orderType,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {

        Pageable pageable = PageRequest.of(page - 1, size);

        // Chuyển đổi ngày từ String sang LocalDate
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        Page<Bill> billPage = billService.searchBills(keyword, orderType, start, end, pageable);

        model.addAttribute("bills", billPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", billPage.getTotalPages());

        return "views/bill";
    }

    @GetMapping("/detail/{id}")
    public String billDetail(@PathVariable("id") Long id, Model model) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isEmpty()) {
            return "redirect:/bill";
        }

        Bill bill = billOptional.get();
        List<BillDetail> billDetails = billDetailRepository.findByBillId(id);
        List<BillDetailDTO> billDetailsp = billDetailService.getBillDetails(id);
        List<BillHistory> billHistorys = billHistotyRepository.findByBill_Id(id);
        List<VoucherDetail> voucherDetails = voucherDetailRepository.findByBillId(id);

        model.addAttribute("bill", bill);
        model.addAttribute("billDetails", billDetails);
        model.addAttribute("billDetailsp", billDetailsp);
        model.addAttribute("billHistorys", billHistorys);
        model.addAttribute("allStatuses", BillStatus.values());
        model.addAttribute("voucherDetail", voucherDetails);

        return "views/billDetail";
    }

}
