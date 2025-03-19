package org.fpoly.capstone.controller;

import jakarta.validation.Valid;
import org.fpoly.capstone.dto.user.AddressDTO;
import org.fpoly.capstone.dto.user.EmployeeDTO;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.EmployeeService;
import org.fpoly.capstone.service.payload.user.FileUploadImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/staff-management")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FileUploadImagesService fileUploadImagesService;

    @Autowired
    private AddressService addressService;

    @GetMapping
    public String listEmployees(Model model) {
        List<User> employees = employeeService.getAllEmployees(); // Lấy danh sách nhân viên có ROLE_USER
        model.addAttribute("employees", employees);
        return "views/users/employee/employee-list";
    }

    @GetMapping("/detail/{id}")
    public String employeeDetail(@PathVariable Long id, Model model) {
        User employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/staff-management";
        }

        Address defaultAddress = addressService.getDefaultAddress(employee.getId());

        model.addAttribute("employee", employee);
        model.addAttribute("defaultAddress", defaultAddress);
        return "views/users/employee/employee-detail";
    }

    @GetMapping("/view-add")
    public String showAddForm(Model model) {
//        EmployeeDTO employeeDTO = new EmployeeDTO();
        User employee = new User();
        employee.setAddresses(new ArrayList<>());
        // Thêm đối tượng vào model để binding dữ liệu với form
        model.addAttribute("employee", employee);
        model.addAttribute("address", new Address());
        return "views/users/employee/employee-create";
    }

    @PostMapping("/add")
    public String saveEmployee(@Valid @ModelAttribute EmployeeDTO employeeDTO,
                               @ModelAttribute AddressDTO addressDTO,
                               @RequestParam("avatarFile") MultipartFile avatarFile) {
        User employee = new User();
        employee.setFullName(employeeDTO.getFullName());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setEmail(employeeDTO.getEmail());
        employee.setGender(employeeDTO.getGender());
        employee.setStatus(employeeDTO.getStatus());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setCitizenIdentity(employeeDTO.getCitizenIdentity());
        employee.setRoles(UserRole.ROLE_USER);
        if (!avatarFile.isEmpty()) {
            String avatarFileName = fileUploadImagesService.saveAvatar(avatarFile, "employee", employee.getId());
            employee.setAvatar(avatarFileName);
        }
        Address address =new Address();
        address.setLine(addressDTO.getLine());
        address.setProvince(addressDTO.getProvince());
        address.setDistrict(addressDTO.getDistrict());
        address.setWard(addressDTO.getWard());
        address.setWardCode(addressDTO.getWardCode());
        address.setWardCode(addressDTO.getProvinceId());
        address.setWardCode(addressDTO.getToDistrictId());
        address.setProvinceId(addressDTO.getProvinceId());
        address.setToDistrictId(addressDTO.getToDistrictId());

        address.setUser(employee);

        if (employee.getAddresses() == null) {
            employee.setAddresses(new ArrayList<>());
        }
        employee.getAddresses().add(address);

        // Lưu vào database
        employeeService.createEmployee(employeeDTO,addressDTO,avatarFile);

        return "redirect:/staff-management";
    }

    @GetMapping("/view-update/{id}")
    public String viewupdateEmployee(@PathVariable Long id, Model model) {
        User employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/staff-management";
        }
        Address defaultAddress = addressService.getDefaultAddress(employee.getId());
        model.addAttribute("employee", employee);
        model.addAttribute("defaultAddress", defaultAddress);
        return "views/users/employee/employee-update";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute User employee,
                                 @ModelAttribute Address address,
                                 @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {
        User existingEmployee = employeeService.getEmployeeById(id);

        if (existingEmployee == null) {
            return "redirect:/staff-management";
        }

        // Cập nhật thông tin nhân viên
        existingEmployee.setFullName(employee.getFullName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        existingEmployee.setDateOfBirth(employee.getDateOfBirth());
        existingEmployee.setGender(employee.getGender());
        existingEmployee.setCitizenIdentity(employee.getCitizenIdentity());
        existingEmployee.setStatus(employee.getStatus());

        // Xử lý ảnh đại diện
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = fileUploadImagesService.saveAvatar(avatarFile, "employee", existingEmployee.getId());
            if (avatarPath != null) {
                existingEmployee.setAvatar(avatarPath);
            }
        }
        // Cập nhật hoặc thêm mới địa chỉ
        Address existingAddress = addressService.getDefaultAddress(existingEmployee.getId());
        if (existingAddress != null) {
            existingAddress.setProvince(address.getProvince());
            existingAddress.setProvinceId(address.getProvinceId());
            existingAddress.setDistrict(address.getDistrict());
            existingAddress.setToDistrictId(address.getToDistrictId());
            existingAddress.setWard(address.getWard());
            existingAddress.setWardCode(address.getWardCode());
            existingAddress.setLine(address.getLine());
        } else {
            address.setUser(existingEmployee);
            if (existingEmployee.getAddresses() == null) {
                existingEmployee.setAddresses(new ArrayList<>());
            }
            existingEmployee.getAddresses().add(address);
        }

        addressService.saveAddress(existingAddress != null ? existingAddress : address);
        employeeService.saveEmployee(existingEmployee);

        return "redirect:/staff-management";
    }

    @GetMapping("/delete/{id}")
    public String softDeleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            employee.setStatus(UserStatus.DELETED); // Đánh dấu là đã xóa
            employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Nhân viên đã được vô hiệu hóa.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên.");
        }
        return "redirect:/staff-management";
    }
}
