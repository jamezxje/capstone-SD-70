package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;

import java.util.List;

public interface CustomerService {
    List<User> getAllCustomers();
    User getCustomerById(Long id);
    User saveCustomer(User customer);
    User updateCustomer(Long id, User customer);
    void deleteCustomer(Long id);
}
