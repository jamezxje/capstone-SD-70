package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    List<User> findAllCustomers();
}
