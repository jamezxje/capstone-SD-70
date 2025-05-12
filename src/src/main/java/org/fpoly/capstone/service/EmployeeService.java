package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

    User getEmployeeById(Long id);
//    User createEmployee(User user , Address address);
//    User updateEmployee(Long id, User user, Address address);
    User createEmployee(User user , Address address);
    User updateEmployee(Long id, User user, Address address);

    //Phân trang tìm kiếm
    Page<User> getEmployeesPaginated(Pageable pageable);
    Page<User> searchAndFilterEmployees(String keyword, String status, Pageable pageable);
}
