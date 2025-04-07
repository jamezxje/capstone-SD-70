package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.repository.EmployeeRepository;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.EmployeeService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

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
    public Page<User> getEmployeesPaginated(Pageable pageable) {
        return employeeRepository.findEmployeesSortedByLastModifiedDate(UserRole.ROLE_USER, pageable);
    }

@Override
public Page<User> searchAndFilterEmployees(String keyword, String status, Pageable pageable) {
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
    return employeeRepository.searchAndFilterEmployees(keyword, userStatus, UserRole.ROLE_USER, pageable);
}
    @Override
    public User getEmployeeById(Long id) {
        return employeeRepository.findEmployAddresses(id).orElse(null);
    }

    @Transactional
    @Override
    public User createEmployee(User user, Address address, MultipartFile file) {
        String rawPassword = PasswordUtil.generateRandomPassword(8); // Tạo mật khẩu 8 ký tự
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String userServiceName = userService.getName();

        // xử lý ảnh
        String urlAvatar = null;
        if (file != null && !file.isEmpty()) {
            urlAvatar = cloudinaryServiceImpl.uploadAvatar(file);
        }
        // Tạo đối tượng User bằng Builder
        User newUser = User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(encodedPassword)
                .avatar(urlAvatar) // Đường dẫn ảnh từ Cloudinary
                .dateOfBirth(user.getDateOfBirth())
                .citizenIdentity(user.getCitizenIdentity())
                .gender(user.getGender())
                .roles(UserRole.ROLE_USER)
                .status(user.getStatus())
                .createdBy(userServiceName)
                .updatedBy(userServiceName)
                .createDate(new Date())
                .lastModifiedDate(new Date())
                .build();
        // Lưu user và lấy ID mới
        User savedUser = employeeRepository.save(newUser);
        System.out.println("User ID: " + savedUser.getId()); // Debug xem có ID không
//        if (address != null) {
            Address newAddress = new Address();
            newAddress.setStatus(AddressStatus.DANG_SU_DUNG);
            newAddress.setProvinceId(address.getProvinceId());
            newAddress.setToDistrictId(address.getToDistrictId());
            newAddress.setWardCode(address.getWardCode());
            newAddress.setProvince(address.getProvince());
            newAddress.setDistrict(address.getDistrict());
            newAddress.setWard(address.getWard());
            newAddress.setLine(address.getLine());
            newAddress.setFullName(user.getFullName());
            newAddress.setPhoneNumber(user.getPhoneNumber());
            newAddress.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            newAddress.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            newAddress.setCreatedBy(userServiceName);
            newAddress.setUpdatedBy(userServiceName);
            newAddress.setUser(savedUser); // Không cần tìm lại user nữa
            // Lưu địa chỉ vào database

            Address savedAddress = addressRepository.save(newAddress);
            System.out.println("Address ID: " + savedAddress.getId()); // Debug xem có lưu không
//        }
        System.out.println("Mật khẩu tài khoản mới: " + rawPassword);
        String subject = "Xin chào, bạn đã đăng ký thành công tài khoản nhân viên CAPSTONE";
        emailServiceImpl.sendEmailPassword(newUser.getEmail(), subject, rawPassword);
        return savedUser;
    }

    @Transactional
    @Override
    public User updateEmployee(Long id, User user, Address address, MultipartFile file) {
        User existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee == null) {
            return null;
        }
        String userServiceName = userService.getName();

        // Nếu có file mới => Upload lên Cloudinary, ngược lại giữ nguyên ảnh cũ
        String urlAvatar = existingEmployee.getAvatar(); // Giữ ảnh cũ
        if (file != null && !file.isEmpty()) {
            urlAvatar = cloudinaryServiceImpl.uploadAvatar(file); // Upload ảnh mới
        }
        // Cập nhật thông tin nhân viên
        existingEmployee.setFullName(user.getFullName());
        existingEmployee.setEmail(user.getEmail());
        existingEmployee.setPhoneNumber(user.getPhoneNumber());
        existingEmployee.setAvatar(urlAvatar); // Cập nhật avatar
        existingEmployee.setDateOfBirth(user.getDateOfBirth());
        existingEmployee.setGender(user.getGender());
        existingEmployee.setCitizenIdentity(user.getCitizenIdentity());
        existingEmployee.setStatus(user.getStatus() == null ? UserStatus.ACTIVATED : user.getStatus());
        existingEmployee.setUpdatedBy(userServiceName);
        existingEmployee.setLastModifiedDate(new Date());
        User savedUser = employeeRepository.save(existingEmployee);
        // Cập nhật hoặc thêm mới địa chỉ
        Address existingAddress = addressService.getDefaultAddress(existingEmployee.getId());
        if (existingAddress != null) {
            existingAddress.setStatus(AddressStatus.DANG_SU_DUNG);
            existingAddress.setProvinceId(address.getProvinceId());
            existingAddress.setToDistrictId(address.getToDistrictId());
            existingAddress.setWardCode(address.getWardCode());
            existingAddress.setProvince(address.getProvince());
            existingAddress.setDistrict(address.getDistrict());
            existingAddress.setWard(address.getWard());
            existingAddress.setLine(address.getLine());
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
                newAddress.setFullName(user.getFullName());
                newAddress.setPhoneNumber(user.getPhoneNumber());
                newAddress.setUpdatedBy(userServiceName);
                newAddress.setLastModifiedDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                newAddress.setUser(savedUser);
                if (existingEmployee.getAddresses() == null) {
                    existingEmployee.setAddresses(new ArrayList<>());
                }
                existingEmployee.getAddresses().add(newAddress);
                addressService.saveAddress(newAddress);
            }
        // Lưu nhân viên
        return savedUser;
    }
}
