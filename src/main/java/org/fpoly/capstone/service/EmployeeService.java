package org.fpoly.capstone.service;

import org.fpoly.capstone.dto.user.AddressDTO;
import org.fpoly.capstone.dto.user.EmployeeDTO;
import org.fpoly.capstone.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    List<User> getAllEmployees();
    User getEmployeeById(Long id);
    User saveEmployee(User employee);
    User createEmployee(EmployeeDTO employeRequest , AddressDTO addressRequest,
                        MultipartFile file);
    User updateEmployee(Long id, User employee);
    void deleteEmployee(Long id);
}
