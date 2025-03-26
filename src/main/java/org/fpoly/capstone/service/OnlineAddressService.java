package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.service.payload.address.CreateAddressRequest;
import org.fpoly.capstone.service.payload.address.UpdateAddressRequest;

import java.util.List;

public interface OnlineAddressService {
    void addNewAddressForOnlineUser(CreateAddressRequest request);

    void updateAddressForOnlineUser(Integer addressId, UpdateAddressRequest request);

    List<Address> getListAddressByLoggedUser();

    Address findDefaultAddressByUserId();

    Address findAddressById(Integer addressId);

    void deleteAddress(Integer addressId);

    void setDefaultAddress(Integer addressId);
}
