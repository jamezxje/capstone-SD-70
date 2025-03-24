package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    Address getDefaultAddress(Long userId);
    Address saveAddress(Address address);
//    Address getAddressesByUserId(Long userId);
}

