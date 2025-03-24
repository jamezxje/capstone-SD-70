package org.fpoly.capstone.controller.user_online.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.OnlineAddressService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.VnPayService;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@Log4j2
@RequestMapping(path = "bill")
@RequiredArgsConstructor
public class ApiOnlineBillController {

    private final CartDetailService cartDetailService;
    private final BillService billService;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OnlineAddressService onlineAddressService;
    private final VnPayService vnPayService;

    @PostMapping("save")
    public ResponseEntity<?> onSaveBillOnline(@RequestBody CreateBillRequest createBillRequest) {
        try {
            User loggedUser = this.userService.getUserFromContext();
            Cart cart = this.cartRepository.findCartByUserId(loggedUser.getId());
            this.billService.saveToBillForOnlineUser(cart, createBillRequest);

            log.info("Bill saved successfully");

            // Return a successful response
            return new ResponseEntity<>("Bill saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception here for better traceability
            e.printStackTrace();

            log.info("Failed to save bill");

            // Return an error response with appropriate status
            return new ResponseEntity<>("Failed to save bill: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping(path = "online-payment")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) throws UnsupportedEncodingException {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = this.vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return vnpayUrl;
    }


}
