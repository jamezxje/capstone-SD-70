package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CustomerRepository;
import org.fpoly.capstone.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.fpoly.capstone.entity.enum_status.UserRole;


import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public List<User> findAllCustomers() {
        return customerRepository.findAll();
        }
    @Override
    public List<User> getAllCustomers() {
        return customerRepository.findByRoles(UserRole.ROLE_CUSTOMER);
    }

    @Override
    public User getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public User saveCustomer(User customer) {
        customer.setRoles(UserRole.ROLE_CUSTOMER);
        return customerRepository.save(customer);
    }

    @Override
    public User updateCustomer(Long id, User customer) {
        Optional<User> existingCustomer = customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
            customer.setId(id);
            customer.setRoles(UserRole.ROLE_CUSTOMER);
            return customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        }
}
