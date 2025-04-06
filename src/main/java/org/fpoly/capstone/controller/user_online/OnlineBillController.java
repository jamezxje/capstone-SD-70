package org.fpoly.capstone.controller.user_online;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.controller.payload.bill_detail.BillDetailViewModel;
import org.fpoly.capstone.controller.payload.cart_detail.CartDetailViewModel;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.PaymentMethod;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.service.BillDetailService;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.OnlineAddressService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.VnPayService;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.fpoly.capstone.service.payload.bill_detail.BillDetailResponse;
import org.fpoly.capstone.service.payload.cart_detail.CartDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Log4j2
@RequestMapping(path = "bill")
@RequiredArgsConstructor
public class OnlineBillController {

    private final CartDetailService cartDetailService;
    private final BillService billService;
    private final BillDetailService billDetailService;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OnlineAddressService onlineAddressService;
    private final VnPayService vnPayService;

    @GetMapping(path = "checkout")
    public String onOpenCheckoutView(Model model) {

        User loggedUser = this.userService.getUserFromContext();

        Address defaultAddress = this.onlineAddressService.findDefaultAddressByUserId();

        List<CartDetailResponse> cartDetailResponseList = this.cartDetailService.findCartDetailByUserId();

        List<CartDetailViewModel> viewModels = cartDetailResponseList.stream()
                .map(response -> this.modelMapper.map(response, CartDetailViewModel.class))
                .toList();

        List<Address> addressList = this.onlineAddressService.getListAddressByLoggedUser();

        model.addAttribute("cartDetailList", viewModels);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("shoppingCart", this.cartRepository.findCartByUserId(loggedUser.getId()));
        model.addAttribute("addressList", addressList);
        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("cartTotalMoney", this.cartRepository.findCartByUserId(loggedUser.getId()).getTotalPrice());
        model.addAttribute("createBillRequest", new CreateBillRequest());


        return "/views/user-online-view/checkout-form";
    }

//    @PostMapping("save")
//    public String onSaveBill(Model model, @ModelAttribute("createBillRequest") CreateBillRequest createBillRequest) {
//        User loggedUser = this.userService.getUserFromContext();
//        Cart cart = this.cartRepository.findCartByUserId(loggedUser.getId());
//        this.billService.saveToBillForOnlineUser(cart, createBillRequest);
//        return "redirect:/bill";
//    }

    @GetMapping(path = "")
    public String onOpenBillView(Model model) {
        User loggedUser = this.userService.getUserFromContext();
        List<Bill> billList = this.billService.findBillsByCustomerId(loggedUser.getId());
        model.addAttribute("billList", billList);
        return "/views/user-online-view/bill/bill-management";
    }

    @GetMapping(path = "{billId}")
    public String onOpenBillDetail(@PathVariable("billId") Long billId,
                                   Model model) {

        List<BillDetailResponse> billDetailResponseList =
                this.billDetailService.findBillDetailByBillId(billId);

        List<BillDetailViewModel> viewModels = billDetailResponseList.stream()
                .map(response -> this.modelMapper.map(response, BillDetailViewModel.class))
                .toList();

        model.addAttribute("billDetailResponseList", viewModels);

        return "/views/user-online-view/bill/bill-detail";
    }

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        // Kiểm tra trạng thái thanh toán từ VNPAY
        int paymentStatus = this.vnPayService.orderReturn(request);

        // Lấy thông tin từ tham số URL
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        // Giải mã orderInfo để lấy receiveDate và moneyShip
        String[] orderInfoParts = orderInfo.split(", "); // Tách chuỗi theo dấu phẩy và khoảng trắng
        String receiveDate = null;
        String moneyShip = null;

        // Kiểm tra và lấy giá trị từ orderInfo
        for (String part : orderInfoParts) {
            if (part.startsWith("data_receiveDate")) {
                receiveDate = part.split(": ")[1].trim(); // Lấy ngày nhận hàng
            } else if (part.startsWith("data_shipFee")) {
                moneyShip = part.split(": ")[1].trim(); // Lấy phí vận chuyển
            }
        }

        // Kiểm tra nếu nhận được thông tin ngày nhận hàng và phí vận chuyển
        if (receiveDate != null && moneyShip != null) {
            log.info("recieve date: {}", receiveDate);
            log.info("ship fee: {}", moneyShip);
            // Nếu thanh toán thành công, lưu hóa đơn vào cơ sở dữ liệu
            if (paymentStatus == 1) {
                try {
                    User loggedUser = this.userService.getUserFromContext();
                    Cart cart = this.cartRepository.findCartByUserId(loggedUser.getId());

                    // Tạo đối tượng CreateBillRequest để lưu hóa đơn
                    CreateBillRequest createBillRequest = new CreateBillRequest();
                    createBillRequest.setGrandTotal(BigDecimal.valueOf(Long.parseLong(totalPrice) / 100)); // Tổng giá trị hóa đơn
                    createBillRequest.setPaymentMethod(PaymentMethod.CHUYEN_KHOAN); // Phương thức thanh toán
                    createBillRequest.setReceiveDate(this.parseDate(receiveDate)); // Lấy từ orderInfo và chuyển đổi thành Date
                    createBillRequest.setMoneyShip(BigDecimal.valueOf(Long.parseLong(moneyShip))); // Lấy từ orderInfo

                    // Lưu hóa đơn vào cơ sở dữ liệu
                    this.billService.saveToBillForOnlineUser(cart, createBillRequest);

                    log.info("Bill saved successfully after VNPAY payment.");

                    return "redirect:/bill";
                } catch (Exception e) {
                    log.error("Failed to save bill after VNPAY payment", e);
                    return "/views/user-online-view/vn-pay/orderFail";
                }
            } else {
                return "/views/user-online-view/vn-pay/orderFail";
            }
        } else {
            log.error("Failed to parse order info from VNPAY response.");
            return "/views/user-online-view/vn-pay/orderFail"; // Trả về trang lỗi nếu không tìm thấy ngày nhận hàng hoặc phí vận chuyển
        }
    }

    // Phương thức để chuyển đổi chuỗi ngày thành đối tượng Date
    private Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Trả về null nếu không thể chuyển đổi ngày
        }
    }


}
