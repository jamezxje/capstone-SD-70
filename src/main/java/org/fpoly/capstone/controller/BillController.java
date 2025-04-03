package org.fpoly.capstone.controller;

import org.fpoly.capstone.entity.ProductDetail;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.Voucher;
import org.fpoly.capstone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;





import org.fpoly.capstone.dto.billDetail.BillDetailDTO;
import org.fpoly.capstone.dto.voucherDetail.VoucherDetailDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.repository.*;
import org.fpoly.capstone.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private BillService billService;

    @Autowired
    private BillDetailService billDetaiService;

    @Autowired
    private BillHistotyRepository billHistotyRepository;

    @Autowired
    private VoucherDetailService voucherDetailService;

    @Autowired
    private BillHistoryService billHistoryService;

    @GetMapping("/listBill")
    public String listBills(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String orderType,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {

        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size); // Đảm bảo không có giá trị âm

        LocalDate start = null;
        LocalDate end = null;

        try {
            if (startDate != null && !startDate.trim().isEmpty()) {
                start = LocalDate.parse(startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                end = LocalDate.parse(endDate);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ngày không hợp lệ! Vui lòng nhập đúng định dạng yyyy-MM-dd.");
            return "views/bill"; // Trả về trang với thông báo lỗi
        }

        Page<Bill> billPage = billService.searchBills(keyword, orderType, start, end, pageable);

        model.addAttribute("bills", billPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", billPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("orderType", orderType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "views/bill";
    }

    @GetMapping("/detail/{id}")
    public String billDetail(@PathVariable("id") Long id, Model model) {
        Optional<Bill> billOptional = billRepository.findById(id);
        if (billOptional.isEmpty()) {
            return "redirect:/bill";
        }

        Bill bill = billOptional.get();
        List<BillDetailDTO> billDetails = billDetaiService.getBillDetails(id); // ✅ Lấy dữ liệu từ service
        List<BillHistory> billHistorys = billHistotyRepository.findByBillId(id);
        // Lấy dữ liệu VoucherDetail từ Service (trả về DTO)
        Optional<VoucherDetailDTO> voucherDetail = voucherDetailService.getVoucherDetailsByBillId(id);

        model.addAttribute("bill", bill);
        model.addAttribute("billDetails", billDetails);
        model.addAttribute("billHistorys", billHistorys);
        model.addAttribute("allStatuses", BillStatus.values());

        // Nếu có dữ liệu VoucherDetail, thêm vào Model
        voucherDetail.ifPresent(dto -> model.addAttribute("voucherDetail", dto));

        return "views/billDetail";
    }
}
