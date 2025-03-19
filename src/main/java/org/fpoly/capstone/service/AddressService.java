package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;

public interface AddressService {
    Address getDefaultAddress(Long userId);
    Address saveAddress(Address address);
}

