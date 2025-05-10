package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.service.payload.addressCustomer.CreateAddressRequest;
import org.fpoly.capstone.service.payload.addressCustomer.UpdateAddressRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    Address getDefaultAddress(Long userId);
    Address saveAddress(Address address);
//    Address getAddressesByUserId(Long userId);

    void addNewAddressForCustomer(CreateAddressRequest request, Long userId);
    void updateAddressForCustomer(Integer addressId, UpdateAddressRequest request);
    List<Address> getListAddressByCustomer(Long userId);
    Address findDefaultAddressByUserId(Long userId);
    Address findAddressById(Integer addressId);
    void deleteAddress(Integer addressId);
    void setDefaultAddress(Integer addressId);
}

