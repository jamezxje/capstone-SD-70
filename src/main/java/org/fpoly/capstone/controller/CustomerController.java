package org.fpoly.capstone.controller;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/customer")
    public String listCustomers(Model model) {
        List<User> customer = customerService.getAllCustomers();
        model.addAttribute("customer", customer);
        return "views/users/customer/customer-list";
    }

    @GetMapping("/customer/view-add")
    public String showAddForm(Model model) {
        User customer = new User();
        customer.setAddresses(new ArrayList<>()); // Khởi tạo danh sách địa chỉ rỗng
        model.addAttribute("customer", customer);
        model.addAttribute("address", new Address());
        return "views/users/customer/customer-create";
    }

    @PostMapping("/customer/add")
    public String saveCustomer(@ModelAttribute User customer) {
        customerService.saveCustomer(customer);
        return "redirect:/dashboard/customer";
    }

    @GetMapping("/customer/view-update/{id}")
    public String viewupdateEmployee(@PathVariable Long id, Model model) {
        User customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/dashboard/customer";
        }
        Address defaultAddress = addressService.getDefaultAddress(customer.getId());

        model.addAttribute("customer", customer);
        model.addAttribute("defaultAddress", defaultAddress);
        return "views/users/customer/customer-update";
    }

    @PostMapping("/customer/update/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute User customer) {
        customerService.updateCustomer(id, customer);
        return "redirect:/dashboard/customer";
    }


    @GetMapping("/customer/detail/{id}")
    public String viewCustomerDetail(@PathVariable Long id, Model model) {
    User customer = customerService.getCustomerById(id);
    if (customer == null) {
        return "redirect:/dashboard/customer";
    }

    Address defaultAddress = addressService.getDefaultAddress(customer.getId());

    model.addAttribute("customer", customer);
    model.addAttribute("defaultAddress", defaultAddress);
    return "views/users/customer/customer-detail";
    }

    @GetMapping("/customer/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/dashboard/customer";
    }
}
