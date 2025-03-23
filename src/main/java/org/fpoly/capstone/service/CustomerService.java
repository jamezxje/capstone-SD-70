package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
//    List<User> getAllCustomers();
//    Page<User> getCustomerPaginated(Pageable pageable);
//    User getCustomerById(Long id);
//    User saveCustomer(User customer);
//    User updateCustomer(Long id, User customer);
//    void deleteCustomer(Long id);

    Page<User> getCustomerPaginated(Pageable pageable);
    User getCustomerById(String id);
    User createCustomer(User user , Address address);
    User updateCustomer(String id, User user, Address address);
}
