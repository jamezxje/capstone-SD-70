package org.fpoly.capstone.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.CustomerService;
import org.fpoly.capstone.service.payload.address.CreateAddressRequest;
import org.fpoly.capstone.service.payload.address.UpdateAddressRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Lazy
    @Autowired
    private CustomerService customerService;

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return addressRepository.findDefaultAddressByUserId(userId, AddressStatus.DANG_SU_DUNG).orElse(null);
    }
//-------------------------------------------------------

    @Override
    public void addNewAddressForCustomer(CreateAddressRequest request, Long id) {
        User customer = customerService.getCustomerById(id);

        Address address = Address.builder()
                .province(request.getProvince())
                .provinceId(request.getProvinceId())  // Đảm bảo provinceId là Integer
                .district(request.getDistrict())
                .toDistrictId(request.getDistrictId())
                .ward(request.getWard())
                .wardCode(request.getWardCode())
                .detailAddress(request.getDetailAddress())
                .user(customer)
                .build();
        List<Address> existingAddressList = this.addressRepository.findAddressByUserId(customer.getId());

        if (existingAddressList.isEmpty()) {
            address.setStatus(AddressStatus.DANG_SU_DUNG);
        } else {
            for (Address existingAddress : existingAddressList) {
                existingAddress.setStatus(AddressStatus.NGUNG_SU_DUNG);
                addressRepository.save(existingAddress);
            }
            address.setStatus(AddressStatus.DANG_SU_DUNG);
        }
        this.addressRepository.save(address);
    }

    @Override
    public void updateAddressForCustomer(Integer addressId, UpdateAddressRequest request) {
        Address existingAddress = this.addressRepository
                .findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy địa chỉ với ID:" + addressId));
        existingAddress.setDistrict(request.getDistrict());
        existingAddress.setToDistrictId(request.getDistrictId());
        existingAddress.setProvince(request.getProvince());
        existingAddress.setProvinceId(request.getProvinceId());
        existingAddress.setWard(request.getWard());
        existingAddress.setWardCode(request.getWardCode());
        existingAddress.setDetailAddress(request.getDetailAddress());
        // Save the updated address
        this.addressRepository.save(existingAddress);
    }

    @Override
    public List<Address> getListAddressByCustomer(Long userId) {
        return addressRepository.findAddressByUserId(userId);
    }

    @Override
    public Address findDefaultAddressByUserId(Long userId) {
        return addressRepository.findDefaultAddressByUserId(userId, AddressStatus.DANG_SU_DUNG)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy địa chỉ mặc định"));
    }

    @Override
    public Address findAddressById(Integer addressId) {
        return addressRepository.findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + addressId));
    }

    @Override
    public void deleteAddress(Integer addressId) {
        Address deleteAddress = addressRepository.findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + addressId));

        addressRepository.delete(deleteAddress);
    }

    @Override
    public void setDefaultAddress(Integer addressId) {
        // Tìm địa chỉ mặc định từ ID
        Address defaultAddress = this.addressRepository
                .findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + addressId));

        // Cập nhật tất cả các địa chỉ còn lại thành 'NGUNG_SU_DUNG'
        for (Address address : this.addressRepository.findAll()) {
            if (!address.getId().equals(defaultAddress.getId())) {
                address.setStatus(AddressStatus.NGUNG_SU_DUNG);
                this.addressRepository.save(address);
            }
        }

        // Đặt địa chỉ mặc định với trạng thái 'DANG_SU_DUNG'
        defaultAddress.setStatus(AddressStatus.DANG_SU_DUNG);
        this.addressRepository.save(defaultAddress);
    }

}

