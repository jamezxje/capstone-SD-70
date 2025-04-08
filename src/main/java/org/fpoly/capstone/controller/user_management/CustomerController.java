package org.fpoly.capstone.controller.user_management;

import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.CustomerService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.validation.AddressValidator;
import org.fpoly.capstone.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/customer-management")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @InitBinder("address")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("status"); // Chặn status chỉ của Address
    }

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
    public String listCustomersPage(@RequestParam(defaultValue = "1") Integer numPage,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String status,
                                    Model model) {
        int size = 5; // Số nhân viên trên mỗi trang
        Pageable pageable = PageRequest.of(numPage - 1, size);
        Page<User> customer;
        if ((keyword != null && !keyword.isEmpty()) || (status != null && !status.isEmpty())) {
            customer = customerService.searchAndFilterCustomer(keyword, status, pageable);
        } else {
            customer = customerService.getCustomerPaginated(pageable);
        }
        model.addAttribute("customer", customer);
        model.addAttribute("currentPage", numPage);
        model.addAttribute("totalPages", customer.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
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
//                               @RequestParam("file") MultipartFile file,
                               @RequestParam("avatar") String avatarUrl,
                               Model model, RedirectAttributes redirectAttributes) {
        Set<String> userFieldsToValidate = Set.of("fullName", "dateOfBirth", "phoneNumber", "email", "gender");
        Map<String, String> errors = UserValidator.validate(user, userFieldsToValidate);
        Set<String> addressFieldsToValidate = Set.of("line", "wardCode", "provinceId", "toDistrictId");
        Map<String, String> errorsAddress = AddressValidator.validate(address, addressFieldsToValidate);
        if (userService.existsByEmail(user.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            errors.put("phoneNumber", "Số điện thoại đã tồn tại!");
        }
        if (!errors.isEmpty() || !errorsAddress.isEmpty() || avatarUrl == null || avatarUrl.isEmpty()) {
            user.setAddresses(new ArrayList<>(List.of(address)));
            model.addAttribute("errors", errors);
            model.addAttribute("errorsAddress", errorsAddress);
            if (avatarUrl == null || avatarUrl.isEmpty()) {
                model.addAttribute("fileError", "Vui lòng chọn ảnh đại diện.");
            }
            return "views/users/customer/customer-create";
        }
        user.setAvatar(avatarUrl);
        customerService.createCustomer(user, address);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm thành công!");
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
//                                 @RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "avatar", required = false) String avatar,
                                 Model model, RedirectAttributes redirectAttributes) {
        User existingUser = customerService.getCustomerById(id);

        boolean isNewAvatarRequired = (avatar == null || avatar.isBlank()) && existingUser.getAvatar() == null;
        Set<String> userFieldsToValidate = Set.of("fullName", "dateOfBirth", "phoneNumber", "email", "gender");
        Map<String, String> errors = UserValidator.validate(user, userFieldsToValidate);
        Set<String> addressFieldsToValidate = Set.of("line", "wardCode", "provinceId", "toDistrictId");
        Map<String, String> errorsAddress = AddressValidator.validate(address, addressFieldsToValidate);
        if (!user.getEmail().equals(existingUser.getEmail()) && userService.existsByEmail(user.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (!user.getPhoneNumber().equals(existingUser.getPhoneNumber()) && userService.existsByPhoneNumber(user.getPhoneNumber())) {
            errors.put("phoneNumber", "Số điện thoại đã tồn tại!");
        }
        if (!errors.isEmpty() || !errorsAddress.isEmpty() || isNewAvatarRequired) {
            // Gán lại avatar cũ nếu có, để giữ ảnh hiển thị khi reload form
            if (existingUser.getAvatar() != null && (user.getAvatar() == null || user.getAvatar().isBlank())) {
                user.setAvatar(existingUser.getAvatar());
            }
            if (isNewAvatarRequired) {
                model.addAttribute("fileError", "Vui lòng chọn ảnh đại diện.");
            }
            user.setAddresses(new ArrayList<>(List.of(address)));
            model.addAttribute("errors", errors);
            model.addAttribute("errorsAddress", errorsAddress);
            model.addAttribute("customer", user);
            model.addAttribute("address", address);
            return "views/users/customer/customer-update";
        }
        user.setAvatar(avatar);
        customerService.updateCustomer(id, user, address);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
        return "redirect:/customer-management";
    }
}
