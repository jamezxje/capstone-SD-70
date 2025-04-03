package org.fpoly.capstone.controller.user_management;

import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.CustomerService;
import org.fpoly.capstone.service.payload.addressCustomer.CreateAddressRequest;
import org.fpoly.capstone.service.payload.addressCustomer.UpdateAddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("customer-management-address")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @GetMapping(path = "/address-list/{id}")
    public String onOpenCustomerProfile(@PathVariable Long id, Model model) {
        User customer = customerService.getCustomerById(id);
        List<Address> listAddress = addressService.getListAddressByCustomer(id);

        model.addAttribute("customer", customer);
        model.addAttribute("listAddress", listAddress);
        model.addAttribute("createAddressRequest", new CreateAddressRequest());
        model.addAttribute("updateAddressRequest", new CreateAddressRequest()); // Fix nhầm lẫn
        return "/views/users/customer/address-list";
    }
    @PostMapping(path = "/delete/{id}")
    public String deleteAddress(@PathVariable Integer id,
     @RequestParam("customerId") Integer customerId) {  // Lấy customerId từ query string
        try {
            this.addressService.deleteAddress(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/customer-management-address/address-list/"+customerId;
    }

    @PostMapping(path = "/set-default/{id}")
    public String setDefaultAddress(
            @PathVariable Integer id,
            @RequestParam("customerId") Integer customerId) {  // Lấy customerId từ query string
        try {
            System.out.println("Setting default address ID: " + id + " for customer: " + customerId);
            this.addressService.setDefaultAddress(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/customer-management-address/address-list/" + customerId; // Sử dụng customerId cho đúng
    }

    @PostMapping("/address/{customerId}")
    public ResponseEntity<?> addAddress(@PathVariable Long customerId,
                                        @RequestBody CreateAddressRequest request) {
        addressService.addNewAddressForCustomer(request, customerId);
        return ResponseEntity.ok("Thêm địa chỉ thành công!");
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<String> updateAddress(
            @PathVariable("id") Integer addressId,  // Thống nhất kiểu Long
            @RequestBody UpdateAddressRequest request) {

        addressService.updateAddressForCustomer(addressId, request);
        return ResponseEntity.ok("Cập nhật địa chỉ thành công!");
    }
}