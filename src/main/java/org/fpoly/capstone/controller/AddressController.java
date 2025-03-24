package org.fpoly.capstone.controller;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    // Hiển thị danh sách địa chỉ của khách hàng
//    @GetMapping("/list/{id}")
//    public String listCustomerAddresses(@PathVariable("id") Long userId, Model model) {
//        User customer = userRepository.findById(userId).orElse(null);
//        if (customer == null) {
//            return "redirect:/customer-management"; // Nếu không tìm thấy khách hàng, quay về danh sách khách hàng
//        }
//        model.addAttribute("customer", customer);
//        model.addAttribute("addresses", addressService.getAddressesByUserId(userId));
//        return "views/users/address/address-list";
//    }
}