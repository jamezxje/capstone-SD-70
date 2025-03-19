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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FileUploadImagesService fileUploadImagesService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/employee")
    public String listEmployees(Model model) {
        List<User> employees = this.employeeService.getAllEmployees(); // Lấy danh sách nhân viên có ROLE_USER
        model.addAttribute("employees", employees);
        return "views/users/employee/employee-list";
    }

    @GetMapping("/employee/detail/{id}")
    public String employeeDetail(@PathVariable Long id, Model model) {
        User employee = this.employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/dashboard/employee";
        }

        Address defaultAddress = this.addressService.getDefaultAddress(employee.getId());

        model.addAttribute("employee", employee);
        model.addAttribute("defaultAddress", defaultAddress);
        return "views/users/employee/employee-detail";
    }

    @GetMapping("/employee/view-add")
    public String showAddForm(Model model) {
//        EmployeeDTO employeeDTO = new EmployeeDTO();
        User employee = new User();
        employee.setAddresses(new ArrayList<>());
        // Thêm đối tượng vào model để binding dữ liệu với form
        model.addAttribute("employee", employee);
        model.addAttribute("address", new Address());
        return "views/users/employee/employee-create";
    }

    @PostMapping("/employee/add")
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
            String avatarFileName = this.fileUploadImagesService.saveAvatar(avatarFile, "employee", employee.getId());
            employee.setAvatar(avatarFileName);
        }
        Address address = new Address();
        address.setLine(addressDTO.getLine());
        address.setProvince(addressDTO.getProvince());
        address.setDistrict(addressDTO.getDistrict());
        address.setWard(addressDTO.getWard());
        address.setWardCode(addressDTO.getWardCode());
        address.setWardCode(addressDTO.getProvinceId());
        address.setWardCode(addressDTO.getToDistrictId());
        address.setProvinceId(Integer.valueOf(addressDTO.getProvinceId()));
        address.setToDistrictId(Integer.valueOf(addressDTO.getToDistrictId()));

        address.setUser(employee);

        if (employee.getAddresses() == null) {
            employee.setAddresses(new ArrayList<>());
        }
        employee.getAddresses().add(address);

        // Lưu vào database
        this.employeeService.createEmployee(employeeDTO, addressDTO, avatarFile);

        return "redirect:/dashboard/employee";
    }

    @GetMapping("/employee/view-update/{id}")
    public String viewupdateEmployee(@PathVariable Long id, Model model) {
        User employee = this.employeeService.getEmployeeById(id);
        if (employee == null) {
            return "redirect:/dashboard/employee";
        }
        Address defaultAddress = this.addressService.getDefaultAddress(employee.getId());
        model.addAttribute("employee", employee);
        model.addAttribute("defaultAddress", defaultAddress);
        return "views/users/employee/employee-update";
    }

    @PostMapping("/employee/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute User employee,
                                 @ModelAttribute Address address,
                                 @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {
        User existingEmployee = this.employeeService.getEmployeeById(id);

        if (existingEmployee == null) {
            return "redirect:/dashboard/employee";
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
            String avatarPath = this.fileUploadImagesService.saveAvatar(avatarFile, "employee", existingEmployee.getId());
            if (avatarPath != null) {
                existingEmployee.setAvatar(avatarPath);
            }
        }
        // Cập nhật hoặc thêm mới địa chỉ
        Address existingAddress = this.addressService.getDefaultAddress(existingEmployee.getId());
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

        this.addressService.saveAddress(existingAddress != null ? existingAddress : address);
        this.employeeService.saveEmployee(existingEmployee);

        return "redirect:/dashboard/employee";
    }

    @GetMapping("/employee/delete/{id}")
    public String softDeleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User employee = this.employeeService.getEmployeeById(id);
        if (employee != null) {
            employee.setStatus(UserStatus.DELETED); // Đánh dấu là đã xóa
            this.employeeService.saveEmployee(employee);
            redirectAttributes.addFlashAttribute("successMessage", "Nhân viên đã được vô hiệu hóa.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên.");
        }
        return "redirect:/dashboard/employee";
    }
}
