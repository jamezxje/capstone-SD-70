package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/customer-management")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/detail/{id}")
    public String viewCustomerDetail(@PathVariable String id, Model model) {
        User customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/customer-management";
        }
        log.info("Customer ID: {}", customer.getId());
        Address address = addressService.getDefaultAddress(customer.getId());
        model.addAttribute("customer", customer);
        model.addAttribute("address", address);
        return "views/users/customer/customer-detail";
    }

    @GetMapping
    public String listCustomersPage(@RequestParam(defaultValue = "1") Integer numPage, Model model) {
        int size = 5; // Số nhân viên trên mỗi trang
        // Đảm bảo numPage không nhỏ hơn 1
        if (numPage < 1) {
            numPage = 1;
        }
        Pageable pageable = PageRequest.of(numPage - 1, size);
        Page<User> customer = customerService.getCustomerPaginated(pageable);
        int totalPages = customer.getTotalPages() > 0 ? customer.getTotalPages() : 1;
        // Đảm bảo numPage không lớn hơn totalPages
        if (numPage > totalPages) {
            numPage = totalPages;
        }
        model.addAttribute("customer", customer);
        model.addAttribute("currentPage", numPage);
        model.addAttribute("totalPages", totalPages);
        return "views/users/customer/customer-list";
    }

    @GetMapping("/view-add")
    public String showAddForm(Model model) {
        User customer = new User();
        Address address = new Address();
        customer.setAddresses(new ArrayList<>(List.of(address)));
        model.addAttribute("address", address);
        model.addAttribute("customer", customer);
        return "views/users/customer/customer-create";
    }

    @PostMapping("/add")
    public String saveCustomer(@Valid @ModelAttribute("customer") User user,
                                BindingResult userResult,
                                @Valid @ModelAttribute("address") Address address,
                                BindingResult addressResult,
                                Model model) {
        System.out.println("User nhận từ form: " + user);
        System.out.println("Address nhận từ form: " + address);
        if (userResult.hasErrors() || addressResult.hasErrors()) {
            System.out.println("Validation lỗi user: " + userResult.getAllErrors());
            System.out.println("Validation lỗi address: " + addressResult.getAllErrors());
            model.addAttribute("customer", user);
            model.addAttribute("address", address);
            return "views/users/customer/customer-create";
        }
        System.out.println("ProvinceId: " + address.getProvinceId());
        System.out.println("ToDistrictId: " + address.getToDistrictId());
        System.out.println("WardCode: " + address.getWardCode());
        // Gọi service để tạo nhân viên và địa chỉ
        customerService.createCustomer(user, address);
        return "redirect:/customer-management";
    }

    @GetMapping("/view-update/{id}")
    public String viewupdateCustomer(@PathVariable String id, Model model) {
        User customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/customer-management";
        }
        Address address = addressService.getDefaultAddress(customer.getId());
        model.addAttribute("customer", customer);
        model.addAttribute("address", address);
        return "views/users/customer/customer-update";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable String id,
                                 @Valid @ModelAttribute("customer") User user,
                                 BindingResult userResult,
                                 @Valid @ModelAttribute("address") Address address,
                                 BindingResult addressResult,
                                 Model model) {
        System.out.println("User nhận từ form: " + user);
        System.out.println("Address nhận từ form: " + address);

        // Kiểm tra lỗi validation
        if (userResult.hasErrors() || addressResult.hasErrors()) {
            System.out.println("Validation lỗi user: " + userResult.getAllErrors());
            System.out.println("Validation lỗi address: " + addressResult.getAllErrors());
            model.addAttribute("customer", user);
            model.addAttribute("address", address);
            return "views/users/customer/customer-update";
        }
        System.out.println("ProvinceId: " + address.getProvinceId());
        System.out.println("ToDistrictId: " + address.getToDistrictId());
        System.out.println("WardCode: " + address.getWardCode());
        System.out.println("Province: " + address.getProvince());
        System.out.println("District: " + address.getDistrict());
        System.out.println("Ward: " + address.getWard());
        // Gọi service để cập nhật nhân viên và địa chỉ

        customerService.updateCustomer(id, user, address);
        return "redirect:/customer-management";
    }
}
