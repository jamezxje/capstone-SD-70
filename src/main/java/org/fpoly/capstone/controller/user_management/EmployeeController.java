package org.fpoly.capstone.controller.user_management;

import lombok.extern.slf4j.Slf4j;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.EmployeeService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/staff-management")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @InitBinder("address")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("status");
    }

    @GetMapping("/detail/{id}")
    public String employeeDetail(@PathVariable Long id, Model model) {
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
    public String listEmployeesPage(@RequestParam(defaultValue = "1") Integer numPage,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String status,
                                    Model model) {
        int size = 5; // Số nhân viên mỗi trang
        Pageable pageable = PageRequest.of(numPage - 1, size);
        Page<User> employees;
        if ((keyword != null && !keyword.isEmpty()) || (status != null && !status.isEmpty())) {
            employees = employeeService.searchAndFilterEmployees(keyword, status, pageable);
        } else {
            employees = employeeService.getEmployeesPaginated(pageable);
        }
        model.addAttribute("employees", employees);
        model.addAttribute("currentPage", numPage);
        model.addAttribute("totalPages", employees.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "views/users/employee/employee-list";
    }

    @GetMapping("/view-add")
        public String showAddForm(Model model) {
        User employee = new User();
        Address address = new Address();
        Map<String, String> errors = new HashMap<>();
        Map<String, String> errorsAddress = new HashMap<>();
        employee.setAddresses(new ArrayList<>(List.of(address)));
        model.addAttribute("address", address);
        model.addAttribute("employee", employee);
        model.addAttribute("errors", errors);
        model.addAttribute("errorsAddress", errorsAddress);
        return "views/users/employee/employee-create";
    }

    @PostMapping("/add")
    public String saveEmployee(@ModelAttribute("employee") User user,
                               @ModelAttribute("address") Address address,
                               @RequestParam("file") MultipartFile file,
                               Model model, RedirectAttributes redirectAttributes) {
        Set<String> userFieldsToValidate = Set.of("fullName", "dateOfBirth", "phoneNumber", "email", "citizenIdentity", "gender");
        Map<String, String> errors = UserValidator.validate(user, userFieldsToValidate);
        Set<String> addressFieldsToValidate = Set.of("line", "wardCode", "provinceId", "toDistrictId");
        Map<String, String> errorsAddress = AddressValidator.validate(address, addressFieldsToValidate);
        if (userService.existsByEmail(user.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            errors.put("phoneNumber", "Số điện thoại đã tồn tại!");
        }
        if (userService.existsByCitizenIdentity(user.getCitizenIdentity())) {
            errors.put("citizenIdentity", "Căn cước công dân đã tồn tại!");
        }
        if (!errors.isEmpty() || !errorsAddress.isEmpty() || file == null || file.isEmpty()) {
            user.setAddresses(new ArrayList<>(List.of(address))); // Set lại address vào user
            model.addAttribute("errors", errors);
            model.addAttribute("errorsAddress", errorsAddress);
            if (file == null || file.isEmpty()) {
                model.addAttribute("fileError", "Vui lòng chọn ảnh đại diện.");
            }
            return "views/users/employee/employee-create";
        }

        employeeService.createEmployee(user, address,file);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm thành công!");
        return "redirect:/staff-management";
    }

    @GetMapping("/view-update/{id}")
    public String viewupdateEmployee(@PathVariable Long id, Model model) {
        User employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/staff-management";
        }
        Map<String, String> errors = new HashMap<>();
        Map<String, String> errorsAddress = new HashMap<>();
        Address address = addressService.getDefaultAddress(employee.getId());

        model.addAttribute("employee", employee);
        model.addAttribute("address", address);
        model.addAttribute("errors", errors);
        model.addAttribute("errorsAddress", errorsAddress);
        return "views/users/employee/employee-update";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employee") User user,
                                 @ModelAttribute("address") Address address,
                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 Model model, RedirectAttributes redirectAttributes) {

        User existingUser = employeeService.getEmployeeById(id);
        boolean isFileEmpty = file == null || file.isEmpty();
        boolean isNewAvatarRequired = isFileEmpty && existingUser.getAvatar() == null;

        Set<String> userFieldsToValidate = Set.of("fullName", "dateOfBirth", "phoneNumber", "email", "citizenIdentity", "gender");
        Map<String, String> errors = UserValidator.validate(user, userFieldsToValidate);

        Set<String> addressFieldsToValidate = Set.of("line", "wardCode", "provinceId", "toDistrictId");
        Map<String, String> errorsAddress = AddressValidator.validate(address, addressFieldsToValidate);

        if (!user.getEmail().equals(existingUser.getEmail()) && userService.existsByEmail(user.getEmail())) {
            errors.put("email", "Email đã tồn tại!");
        }
        if (!user.getPhoneNumber().equals(existingUser.getPhoneNumber()) && userService.existsByPhoneNumber(user.getPhoneNumber())) {
            errors.put("phoneNumber", "Số điện thoại đã tồn tại!");
        }
        if (!user.getCitizenIdentity().equals(existingUser.getCitizenIdentity()) && userService.existsByCitizenIdentity(user.getCitizenIdentity())) {
            errors.put("citizenIdentity", "Căn cước công dân đã tồn tại!");
        }
        if (!errors.isEmpty() || !errorsAddress.isEmpty() || isNewAvatarRequired) {
            // Gán lại avatar cũ nếu có, để giữ ảnh hiển thị khi reload form
            if (existingUser.getAvatar() != null) {
                user.setAvatar(existingUser.getAvatar());
            }
            if (isNewAvatarRequired) {
                model.addAttribute("fileError", "Vui lòng chọn ảnh đại diện.");
            }
            user.setAddresses(new ArrayList<>(List.of(address)));
            model.addAttribute("errors", errors);
            model.addAttribute("errorsAddress", errorsAddress);
            model.addAttribute("employee", user);
            model.addAttribute("address", address);

            return "views/users/employee/employee-update";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
        employeeService.updateEmployee(id, user, address, file);
        return "redirect:/staff-management";
    }
}
