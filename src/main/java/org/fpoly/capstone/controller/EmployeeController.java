package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/staff-management")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("/detail/{id}")
    public String employeeDetail(@PathVariable String id, Model model) {
        User employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/staff-management";
        }
        log.info("Employee ID: {}", employee.getId());
        Address address = addressService.getDefaultAddress(employee.getId());
        model.addAttribute("employee", employee);
        model.addAttribute("address", address);
        return "views/users/employee/employee-detail";
    }

    @GetMapping
    public String listEmployeesPage(@RequestParam(defaultValue = "1") Integer numPage, Model model) {
        int size = 5; // Số nhân viên trên mỗi trang
        // Đảm bảo numPage không nhỏ hơn 1
        if (numPage < 1) {
            numPage = 1;
        }
        Pageable pageable = PageRequest.of(numPage - 1, size);
        Page<User> employees = employeeService.getEmployeesPaginated(pageable);
        int totalPages = employees.getTotalPages() > 0 ? employees.getTotalPages() : 1;
        // Đảm bảo numPage không lớn hơn totalPages
        if (numPage > totalPages) {
            numPage = totalPages;
        }
        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", numPage);
        model.addAttribute("totalPages", totalPages);
        return "views/users/employee/employee-list";
    }

    @GetMapping("/view-add")
    public String showAddForm(Model model) {
        User employee = new User();
        Address address = new Address();
        // Gán address vào danh sách địa chỉ của employee
        employee.setAddresses(new ArrayList<>(List.of(address)));
        model.addAttribute("address", address);
        model.addAttribute("employee", employee);
        return "views/users/employee/employee-create";
    }

    @PostMapping("/add")
    public String saveEmployee(@Valid @ModelAttribute("employee") User user,
                               BindingResult userResult,
                               @Valid @ModelAttribute("address") Address address,
                               BindingResult addressResult,
//                               @RequestParam("file") MultipartFile file,
                               Model model) {
        System.out.println("User nhận từ form: " + user);
        System.out.println("Address nhận từ form: " + address);
        if (userResult.hasErrors() || addressResult.hasErrors()) {
            System.out.println("Validation lỗi user: " + userResult.getAllErrors());
            System.out.println("Validation lỗi address: " + addressResult.getAllErrors());
            model.addAttribute("employee", user);
            model.addAttribute("address", address);
            return "views/users/employee/employee-create";
        }
        System.out.println("ProvinceId: " + address.getProvinceId());
        System.out.println("ToDistrictId: " + address.getToDistrictId());
        System.out.println("WardCode: " + address.getWardCode());
        // Gọi service để tạo nhân viên và địa chỉ
//        employeeService.createEmployee(user, address,file);
        employeeService.createEmployee(user, address);
        return "redirect:/staff-management";
    }

    @GetMapping("/view-update/{id}")
    public String viewupdateEmployee(@PathVariable String id, Model model) {
        User employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/staff-management";
        }
        Address address = addressService.getDefaultAddress(employee.getId());
        model.addAttribute("employee", employee);
        model.addAttribute("address", address);
        return "views/users/employee/employee-update";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable String id,
                                 @Valid @ModelAttribute("employee") User user,
                                 BindingResult userResult,
                                 @Valid @ModelAttribute("address") Address address,
                                 BindingResult addressResult,
//                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 Model model) {
        System.out.println("User nhận từ form: " + user);
        System.out.println("Address nhận từ form: " + address);

        // Kiểm tra lỗi validation
        if (userResult.hasErrors() || addressResult.hasErrors()) {
            System.out.println("Validation lỗi user: " + userResult.getAllErrors());
            System.out.println("Validation lỗi address: " + addressResult.getAllErrors());
            model.addAttribute("employee", user);
            model.addAttribute("address", address);
            return "views/users/employee/employee-update";
        }
        System.out.println("ProvinceId: " + address.getProvinceId());
        System.out.println("ToDistrictId: " + address.getToDistrictId());
        System.out.println("WardCode: " + address.getWardCode());
        System.out.println("Province: " + address.getProvince());
        System.out.println("District: " + address.getDistrict());
        System.out.println("Ward: " + address.getWard());
        System.out.println("Avatar: " + user.getAvatar());
        // Gọi service để cập nhật nhân viên và địa chỉ

        employeeService.updateEmployee(id, user, address);
//        employeeService.updateEmployee(id, user, address,file);
        return "redirect:/staff-management";
    }

}
