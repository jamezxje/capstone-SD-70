package org.fpoly.capstone.controller.user_online;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PaymentReturnController {

    @GetMapping("/payment/return")
    public String paymentReturn(@RequestParam Map<String, String> params, Model model) {
        // Giả sử bạn đã kiểm tra tính hợp lệ của dữ liệu từ VNPay
        String txnRef = params.get("vnp_TxnRef");
        String amount = params.get("vnp_Amount");
        String status = params.get("vnp_ResponseCode").equals("00") ? "Thành công" : "Thất bại";
        String message = params.get("vnp_ResponseCode").equals("00") ? "Thanh toán thành công!" : "Thanh toán thất bại. Vui lòng thử lại.";

        model.addAttribute("txnRef", txnRef);
        model.addAttribute("amount", amount);
        model.addAttribute("status", status);
        model.addAttribute("message", message);

        return "paymentReturn";
    }
}
