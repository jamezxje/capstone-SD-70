package org.fpoly.capstone.controller.user_online.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.exceptions.ErrorResponse;
import org.fpoly.capstone.service.BillService;
import org.fpoly.capstone.service.payload.bill.BuyNowBillRequest;
import org.fpoly.capstone.service.payload.bill.CreateBillRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ApiBuyNowController {

    private final BillService billService;

    @PostMapping(path = "buy-now")
    public ResponseEntity<?> onBuyNowProduct(@RequestBody BuyNowBillRequest buyNowBillRequest) {

        try {
            this.billService.buyNowForOnlineUser(buyNowBillRequest);
            return new ResponseEntity<>("Buy now product successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            ErrorResponse errorResponse = new ErrorResponse("Mua sản phẩm thất bại: " + e.getMessage(), "CART_UPDATE_ERROR");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("buy-now/save")
    public ResponseEntity<?> onSaveBillOnlineForBuyNow(@RequestBody CreateBillRequest createBillRequest) {
        try {

            this.billService.saveToBillForBuyNow(createBillRequest);

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
