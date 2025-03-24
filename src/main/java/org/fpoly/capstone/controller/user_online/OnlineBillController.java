package org.fpoly.capstone.controller.user_online;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.controller.payload.bill_detail.BillDetailViewModel;
import org.fpoly.capstone.controller.payload.cart_detail.CartDetailViewModel;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.Bill;
import org.fpoly.capstone.entity.User;
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

import java.util.List;

@Controller
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
        int paymentStatus = this.vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "/views/user-online-view/vn-pay/orderSuccess" : "/views/user-online-view/vn-pay/orderFail";
    }
}
