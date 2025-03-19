package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return addressRepository.findDefaultAddressByUserId(userId).orElse(null);
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
}

