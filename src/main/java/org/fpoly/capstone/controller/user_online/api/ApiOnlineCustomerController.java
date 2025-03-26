package org.fpoly.capstone.controller.user_online.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.service.OnlineAddressService;
import org.fpoly.capstone.service.payload.address.CreateAddressRequest;
import org.fpoly.capstone.service.payload.address.UpdateAddressRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("online/customer")
@RequiredArgsConstructor
public class ApiOnlineCustomerController {

    private final OnlineAddressService onlineAddressService;

    @PostMapping(path = "address")
    public ResponseEntity<?> onCreateCustomerAddress(@RequestBody CreateAddressRequest createAddressRequest) {

        try {
            this.onlineAddressService.addNewAddressForOnlineUser(createAddressRequest);
            return new ResponseEntity<>("Address Create successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to create address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(path = "address/{addressId}")
    public ResponseEntity<?> onUpdateCustomerAddress(@PathVariable("addressId") Integer addressId,
                                                     @RequestBody UpdateAddressRequest updateAddressRequest) {

        try {
            this.onlineAddressService.updateAddressForOnlineUser(addressId, updateAddressRequest);
            return new ResponseEntity<>("Address updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update Address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "address/{addressId}")
    public ResponseEntity<?> fetchAddressById(@PathVariable("addressId") Integer addressId) {

        try {
            Address address = this.onlineAddressService.findAddressById(addressId);
            // If the address is found, return the address as the response
            if (address != null) {
                // Return the address object in the response body
                return new ResponseEntity<>(address, HttpStatus.OK);
            } else {
                // Return a NOT_FOUND response if the address doesn't exist
                return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to get Address: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
