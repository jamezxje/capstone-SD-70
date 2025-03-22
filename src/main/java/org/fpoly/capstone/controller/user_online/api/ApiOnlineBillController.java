package org.fpoly.capstone.controller.user_online.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Cart;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CartRepository;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.CartDetailService;
import org.fpoly.capstone.service.OnlineAddressService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
