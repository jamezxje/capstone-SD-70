package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.repository.CustomerRepository;
import org.fpoly.capstone.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public List<User> findAllCustomers() {
        return customerRepository.findAll();
    }
}
