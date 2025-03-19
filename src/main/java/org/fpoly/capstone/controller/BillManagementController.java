package org.fpoly.capstone.controller;

import org.fpoly.capstone.dto.BillDetailDTO;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.BillDetail;
import org.fpoly.capstone.entity.BillHistory;
import org.fpoly.capstone.entity.VoucherDetail;
import org.fpoly.capstone.entity.enum_status.BillStatus;
import org.fpoly.capstone.repository.BillDetailRepository;
import org.fpoly.capstone.repository.BillHistotyRepository;
import org.fpoly.capstone.repository.BillRepository;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.repository.VoucherDetailRepository;
import org.fpoly.capstone.service.BillDetailService;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.impl.BillDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/bill-management")
public class BillManagementController {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private BillDetailServiceImpl billDetailServiceImpl;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private BillHistotyRepository billHistotyRepository;

    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    @GetMapping
    public String listBills(Model model) {
        model.addAttribute("bills", this.billRepository.findAll());
        model.addAttribute("users", this.userRepository.findAll());
        return "views/bill";
    }

    @GetMapping("/detail/{id}")
    public String billDetail(@PathVariable("id") Long id, Model model) {
        Optional<Bill> billOptional = this.billRepository.findById(id);
        if (billOptional.isEmpty()) {
            return "redirect:/bill";
        }

        Bill bill = billOptional.get();
        List<BillDetail> billDetails = this.billDetailRepository.findByBillId(id);
        List<BillDetailDTO> billDetailsp = this.billDetailService.getBillDetails(id);
        List<BillHistory> billHistorys = this.billHistotyRepository.findByBill_Id(id);
        List<VoucherDetail> voucherDetails = this.voucherDetailRepository.findByBillId(id);

        model.addAttribute("bill", bill);
        model.addAttribute("billDetails", billDetails);
        model.addAttribute("billDetailsp", billDetailsp);
        model.addAttribute("billHistorys", billHistorys);
        model.addAttribute("allStatuses", BillStatus.values());
        model.addAttribute("voucherDetail", voucherDetails);

        return "views/billDetail";
    }

    @PostMapping("/detail/{id}/confirm")
    public String confirmPayment(@PathVariable("id") Long billId,
                                 @RequestParam("note") String note,
                                 RedirectAttributes redirectAttributes) {
        boolean success = this.billDetailServiceImpl.confirmPayment(billId, note);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Trạng thái đơn hàng đã được cập nhật.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật thất bại. Kiểm tra trạng thái hiện tại.");
        }

        return "redirect:/bill/detail/" + billId;
    }

}
