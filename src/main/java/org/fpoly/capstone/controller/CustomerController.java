package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.CustomerService;
import org.fpoly.capstone.validation.AddressValidator;
import org.fpoly.capstone.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/customer-management")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/detail/{id}")
    public String viewCustomerDetail(@PathVariable Long id, Model model) {
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
        Map<String, String> errors = new HashMap<>();
        Map<String, String> errorsAddress = new HashMap<>();

        customer.setAddresses(new ArrayList<>(List.of(address)));
        model.addAttribute("address", address);
        model.addAttribute("customer", customer);
        model.addAttribute("errors", errors);
        model.addAttribute("errorsAddress", errorsAddress);
        return "views/users/customer/customer-create";
    }

    @PostMapping("/add")
    public String saveCustomer(@ModelAttribute("customer") User user,
                                @ModelAttribute("address") Address address,
                                Model model) {
        System.out.println("User nhận từ form: " + user);
        System.out.println("Address nhận từ form: " + address);

        // Validate dữ liệu
        Set<String> userFieldsToValidate = Set.of("fullName", "dateOfBirth", "phoneNumber", "email", "gender");
        Map<String, String> errors = UserValidator.validate(user, userFieldsToValidate);
        Set<String> addressFieldsToValidate = Set.of("line", "wardCode", "provinceId", "toDistrictId");
        Map<String, String> errorsAddress = AddressValidator.validate(address, addressFieldsToValidate);

        if (!errors.isEmpty() || !errorsAddress.isEmpty()) {
            user.setAddresses(new ArrayList<>(List.of(address))); // Set lại address vào user
            model.addAttribute("errors", errors);
            model.addAttribute("errorsAddress", errorsAddress);
            model.addAttribute("employee", user);
            model.addAttribute("address", address);
            return "views/users/employee/customer-create";
        }

        System.out.println("ProvinceId: " + address.getProvinceId());
        System.out.println("ToDistrictId: " + address.getToDistrictId());
        System.out.println("WardCode: " + address.getWardCode());
        // Gọi service để tạo nhân viên và địa chỉ
        customerService.createCustomer(user, address);
        return "redirect:/customer-management";
    }

    @GetMapping("/view-update/{id}")
    public String viewupdateCustomer(@PathVariable Long id, Model model) {
        User customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/customer-management";
        }
        Map<String, String> errors = new HashMap<>();
        Map<String, String> errorsAddress = new HashMap<>();
        Address address = addressService.getDefaultAddress(customer.getId());

        model.addAttribute("customer", customer);
        model.addAttribute("address", address);
        model.addAttribute("errors", errors);
        model.addAttribute("errorsAddress", errorsAddress);
        return "views/users/customer/customer-update";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @ModelAttribute("customer") User user,
                                 @ModelAttribute("address") Address address,
                                 Model model) {
        System.out.println("User nhận từ form: " + user);
        System.out.println("Address nhận từ form: " + address);

        // Validate dữ liệu
        Set<String> userFieldsToValidate = Set.of("fullName", "dateOfBirth", "phoneNumber", "email", "gender");
        Map<String, String> errors = UserValidator.validate(user, userFieldsToValidate);
        Set<String> addressFieldsToValidate = Set.of("line", "wardCode", "provinceId", "toDistrictId");
        Map<String, String> errorsAddress = AddressValidator.validate(address, addressFieldsToValidate);

        if (!errors.isEmpty() || !errorsAddress.isEmpty()) {
            user.setAddresses(new ArrayList<>(List.of(address))); // Set lại address vào user
            model.addAttribute("errors", errors);
            model.addAttribute("errorsAddress", errorsAddress);
            model.addAttribute("employee", user);
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
