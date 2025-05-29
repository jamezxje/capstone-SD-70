package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.repository.CustomerRepository;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.CustomerService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CloudinaryServiceImpl cloudinaryServiceImpl;

    @Override
    public Page<User> getCustomerPaginated(Pageable pageable) {
        return customerRepository.findCustomersSortedByLastModifiedDate(UserRole.ROLE_CUSTOMER, pageable);
    }
    @Override
    public List<User> findAllCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Page<User> searchAndFilterCustomer(String keyword, String status, Pageable pageable) {
        UserStatus userStatus = null;
        // Chuyển đổi status từ String sang Enum UserStatus
        if (status != null && !status.isEmpty()) {
            try {
                userStatus = UserStatus.valueOf(status.toUpperCase()); // Chuyển về chữ hoa
            } catch (IllegalArgumentException e) {
                userStatus = null; // Nếu không khớp với Enum, đặt null để lấy tất cả
            }
        }
        // Nếu keyword rỗng, đặt về null để tránh lỗi truy vấn
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return customerRepository.searchAndFilterCustomer(keyword, userStatus, UserRole.ROLE_CUSTOMER, pageable);
    }

    @Override
    public User getCustomerById(Long id) {
        return customerRepository.findCustomerAddresses(id).orElse(null);
    }

    @Transactional
    @Override
    public User createCustomer(User user, Address address) {
        String rawPassword = PasswordUtil.generateRandomPassword(8); // Tạo mật khẩu 8 ký tự
        String encodedPassword = passwordEncoder.encode(rawPassword);// Mã hóa mật khẩu
        String userServiceName = userService.getName();

//        String urlAvatar = null;
//        if (file != null && !file.isEmpty()) {
//            urlAvatar = cloudinaryServiceImpl.uploadAvatar(file);
//        }

        // Tạo đối tượng user
        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setPassword(encodedPassword);
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setGender(user.getGender());
        newUser.setAvatar(user.getAvatar());
        newUser.setRoles(UserRole.ROLE_CUSTOMER);
        newUser.setStatus(user.getStatus());
        newUser.setCreatedBy(userServiceName);
        newUser.setUpdatedBy(userServiceName);
        newUser.setCreateDate(new Date());
        newUser.setLastModifiedDate(new Date());

        // Lưu user và lấy ID mới
        User savedUser = customerRepository.save(newUser);
        System.out.println("User ID: " + savedUser.getId()); // Debug xem có ID không

        if (address != null) {
            Address newAddress = new Address();
            newAddress.setStatus(AddressStatus.DANG_SU_DUNG);
            newAddress.setProvinceId(address.getProvinceId());
            newAddress.setToDistrictId(address.getToDistrictId());
            newAddress.setWardCode(address.getWardCode());
            newAddress.setProvince(address.getProvince());
            newAddress.setDistrict(address.getDistrict());
            newAddress.setWard(address.getWard());
            newAddress.setLine(address.getLine());
            newAddress.setDetailAddress(address.getLine());
            newAddress.setFullName(user.getFullName());
            newAddress.setPhoneNumber(user.getPhoneNumber());
            newAddress.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            newAddress.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            newUser.setCreatedBy(userServiceName);
            newUser.setUpdatedBy(userServiceName);
            newAddress.setUser(savedUser); // Không cần tìm lại user nữa
            addressRepository.save(newAddress);
        }
        System.out.println("Mật khẩu tài khoản mới: " + rawPassword);
        String subject = "Xin chào, bạn đã đăng ký thành công tài khoản Poly Sport";
        emailServiceImpl.sendEmailPassword(newUser.getEmail(), subject, rawPassword);
        return savedUser;
    }

    @Transactional
    @Override
    public User updateCustomer(Long id, User user, Address address) {
        User existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer == null) {
            return null;
        }
        String userServiceName = userService.getName();

        // Nếu có file mới => Upload lên Cloudinary, ngược lại giữ nguyên ảnh cũ
        String urlAvatar = user.getAvatar(); // Giữ ảnh cũ
//        if (file != null && !file.isEmpty()) {
//            urlAvatar = cloudinaryServiceImpl.uploadAvatar(file); // Upload ảnh mới
//        }

        // Cập nhật thông tin khách hàng
        existingCustomer.setFullName(user.getFullName());
        existingCustomer.setEmail(user.getEmail());
        existingCustomer.setPhoneNumber(user.getPhoneNumber());
        existingCustomer.setDateOfBirth(user.getDateOfBirth());
        existingCustomer.setGender(user.getGender());
        existingCustomer.setStatus(user.getStatus());
        existingCustomer.setAvatar(urlAvatar); // Cập nhật avatar
        existingCustomer.setUpdatedBy(userServiceName);
        existingCustomer.setLastModifiedDate(new Date());
        User savedUser = customerRepository.save(existingCustomer);

        // Cập nhật hoặc thêm mới địa chỉ
        Address existingAddress = addressService.getDefaultAddress(existingCustomer.getId());
        if (existingAddress != null) {
            existingAddress.setStatus(AddressStatus.DANG_SU_DUNG);
            existingAddress.setProvinceId(address.getProvinceId());
            existingAddress.setToDistrictId(address.getToDistrictId());
            existingAddress.setWardCode(address.getWardCode());
            existingAddress.setProvince(address.getProvince());
            existingAddress.setDistrict(address.getDistrict());
            existingAddress.setWard(address.getWard());
            existingAddress.setLine(address.getLine());
            existingAddress.setDetailAddress(address.getLine());
            existingAddress.setFullName(user.getFullName());
            existingAddress.setPhoneNumber(user.getPhoneNumber());
            existingAddress.setUpdatedBy(userServiceName);
            existingAddress.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            existingAddress.setUser(savedUser);
            addressService.saveAddress(existingAddress);
        } else {
            Address newAddress = new Address();
            newAddress.setStatus(AddressStatus.DANG_SU_DUNG);
            newAddress.setProvinceId(address.getProvinceId());
            newAddress.setToDistrictId(address.getToDistrictId());
            newAddress.setWardCode(address.getWardCode());
            newAddress.setProvince(address.getProvince());
            newAddress.setDistrict(address.getDistrict());
            newAddress.setWard(address.getWard());
            newAddress.setLine(address.getLine());
            newAddress.setDetailAddress(address.getLine());
            newAddress.setFullName(user.getFullName());
            newAddress.setPhoneNumber(user.getPhoneNumber());
            newAddress.setUpdatedBy(userServiceName);
            newAddress.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            newAddress.setUser(savedUser);
            if (existingCustomer.getAddresses() == null) {
                existingCustomer.setAddresses(new ArrayList<>());
            }
            existingCustomer.getAddresses().add(newAddress);
            addressService.saveAddress(newAddress);
        }
        // Lưu khách hàng
        return savedUser;
    }

}
