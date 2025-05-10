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

        // Tạo địa chỉ mới từ request
        Address address = Address.builder()
                .fullName(request.getFullName()) // Thêm fullName
                .phoneNumber(request.getPhoneNumber()) // Thêm phoneNumber
                .province(request.getProvince())
                .provinceId(String.valueOf(request.getProvinceId()))
                .district(request.getDistrict())
                .toDistrictId(String.valueOf(request.getDistrictId()))
                .ward(request.getWard())
                .wardCode(String.valueOf(request.getWardCode()))
                .detailAddress(request.getDetailAddress())
                .user(loggedUser)
                .build();


        List<Address> existingAddressList = this.onlineAddressRepository.findAddressByUserId(loggedUser.getId());

        if (existingAddressList.isEmpty()) {
            address.setStatus(AddressStatus.DANG_SU_DUNG);
        } else {

            for (Address existingAddress : existingAddressList) {
                existingAddress.setStatus(AddressStatus.NGUNG_SU_DUNG);
                this.onlineAddressRepository.save(existingAddress);
            }
            address.setStatus(AddressStatus.DANG_SU_DUNG);
        }

        // Lưu địa chỉ mới vào cơ sở dữ liệu
        this.onlineAddressRepository.save(address);
    }


    @Override
    public void updateAddressForOnlineUser(Integer addressId, UpdateAddressRequest request) {
        Address existingAddress = this.onlineAddressRepository
                .findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id:" + addressId));
        existingAddress.setFullName(request.getFullName()); // Thêm fullName
        existingAddress.setPhoneNumber(request.getPhoneNumber()); // Thêm phoneNumber
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
    public Address findDefaultAddressByUserId() {
        User loggedUser = this.userService.getUserFromContext();

        return this.onlineAddressRepository.findDefaultAddressByUserId(loggedUser.getId());
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

    @Override
    public void setDefaultAddress(Integer addressId) {
        // Tìm địa chỉ từ ID
        Address defaultAddress = this.onlineAddressRepository
                .findById(Long.valueOf(addressId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + addressId));
        // Lấy userId từ defaultAddress
        Long userId = defaultAddress.getUser().getId();
        // Cập nhật tất cả các địa chỉ của user này thành 'NGUNG_SU_DUNG' trừ địa chỉ mặc định
        List<Address> userAddresses = this.onlineAddressRepository.findAddressByUserId(userId);
        for (Address address : userAddresses) {
            if (!address.getId().equals(defaultAddress.getId())) {
                address.setStatus(AddressStatus.NGUNG_SU_DUNG);
            }
        }
        // Đặt địa chỉ mặc định thành 'DANG_SU_DUNG'
        defaultAddress.setStatus(AddressStatus.DANG_SU_DUNG);

        // Lưu tất cả thay đổi
        this.onlineAddressRepository.saveAll(userAddresses);
        this.onlineAddressRepository.save(defaultAddress);
    }
//    @Override
//    public void setDefaultAddress(Integer addressId) {
//        // Tìm địa chỉ mặc định từ ID
//        Address defaultAddress = this.onlineAddressRepository
//                .findById(Long.valueOf(addressId))
//                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + addressId));
//
//        // Cập nhật tất cả các địa chỉ còn lại thành 'NGUNG_SU_DUNG'
//        for (Address address : this.onlineAddressRepository.findAll()) {
//            if (!address.getId().equals(defaultAddress.getId())) {
//                address.setStatus(AddressStatus.NGUNG_SU_DUNG);
//                this.onlineAddressRepository.save(address);
//            }
//        }
//
//        // Đặt địa chỉ mặc định với trạng thái 'DANG_SU_DUNG'
//        defaultAddress.setStatus(AddressStatus.DANG_SU_DUNG);
//        this.onlineAddressRepository.save(defaultAddress);
//    }

}
