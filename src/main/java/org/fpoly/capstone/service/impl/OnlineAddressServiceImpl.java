package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.fpoly.capstone.repository.OnlineAddressRepository;
import org.fpoly.capstone.service.OnlineAddressService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.address.CreateAddressRequest;
import org.fpoly.capstone.service.payload.address.UpdateAddressRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OnlineAddressServiceImpl implements OnlineAddressService {

    private final OnlineAddressRepository onlineAddressRepository;
    private final UserService userService;

    @Override
    public void addNewAddressForOnlineUser(CreateAddressRequest request) {

        User loggedUser = this.userService.getUserFromContext();

        Address address = Address.builder()
                .province(request.getProvince())
                .provinceId(String.valueOf(request.getProvinceId()))
                .district(request.getDistrict())
                .toDistrictId(String.valueOf(request.getDistrictId()))
                .ward(request.getWard())
                .wardCode(String.valueOf(request.getWardCode()))
                .detailAddress(request.getDetailAddress())
                .user(loggedUser)
                .status(AddressStatus.DANG_SU_DUNG)
                .build();

        this.onlineAddressRepository.save(address);

    }

    @Override
    public void updateAddressForOnlineUser(Integer addressId, UpdateAddressRequest request) {
        Address existingAddress = this.onlineAddressRepository
                .findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id:" + addressId));

        existingAddress.setDistrict(request.getDistrict());
        existingAddress.setToDistrictId(String.valueOf(request.getDistrictId()));
        existingAddress.setProvince(request.getProvince());
        existingAddress.setProvinceId(String.valueOf(request.getProvinceId()));
        existingAddress.setWard(request.getWard());
        existingAddress.setWardCode(String.valueOf(request.getWardCode()));
        existingAddress.setDetailAddress(request.getDetailAddress());

        // Save the updated address
        this.onlineAddressRepository.save(existingAddress);
    }

    @Override
    public List<Address> getListAddressByLoggedUser() {
        User loggedUser = this.userService.getUserFromContext();

        return this.onlineAddressRepository.findAddressByUserId(loggedUser.getId());
    }

    @Override
    public Address findAddressById(Integer addressId) {
        return this.onlineAddressRepository
                .findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id:" + addressId));
    }

    @Override
    public void deleteAddress(Integer addressId) {
        Address deleteAdress = this.onlineAddressRepository
                .findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id:" + addressId));

        this.onlineAddressRepository.delete(deleteAdress);
    }
}
