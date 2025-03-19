package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.repository.CustomerRepository;
import org.fpoly.capstone.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<User> findAllCustomers() {
        return this.customerRepository.findAll();
    }

    @Override
    public List<User> getAllCustomers() {
        return this.customerRepository.findByRoles(UserRole.ROLE_CUSTOMER);
    }

    @Override
    public User getCustomerById(Long id) {
        return this.customerRepository.findById(id).orElse(null);
    }

    @Override
    public User saveCustomer(User customer) {
        customer.setRoles(UserRole.ROLE_CUSTOMER);
        return this.customerRepository.save(customer);
    }

    @Override
    public User updateCustomer(Long id, User customer) {
        Optional<User> existingCustomer = this.customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
            customer.setId(id);
            customer.setRoles(UserRole.ROLE_CUSTOMER);
            return this.customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {
        this.customerRepository.deleteById(id);
    }
}
