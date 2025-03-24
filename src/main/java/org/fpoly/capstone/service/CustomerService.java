package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Page<User> getCustomerPaginated(Pageable pageable);
    User getCustomerById(Long id);
    User createCustomer(User user , Address address);
    User updateCustomer(Long id, User user, Address address);
}
