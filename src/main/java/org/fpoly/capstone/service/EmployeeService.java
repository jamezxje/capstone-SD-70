package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface EmployeeService {

    Page<User> getEmployeesPaginated(Pageable pageable);
    User getEmployeeById(String id);
    User createEmployee(User user , Address address);
    User updateEmployee(String id, User user, Address address);
//    User createEmployee(User user , Address address, MultipartFile file);
//    User updateEmployee(String id, User user, Address address, MultipartFile file);

}
