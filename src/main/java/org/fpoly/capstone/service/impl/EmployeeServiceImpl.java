package org.fpoly.capstone.service.impl;

import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.repository.EmployeeRepository;
import org.fpoly.capstone.service.AddressService;
import org.fpoly.capstone.service.EmailService;
import org.fpoly.capstone.service.EmployeeService;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.utils.PasswordUtil;
import org.fpoly.capstone.utils.upload.UploadAvatarToCloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UploadAvatarToCloudinary uploadAvatarToCloudinary;

    @Override
    public Page<User> getEmployeesPaginated(Pageable pageable) {
        return employeeRepository.findByRolesAndStatus(UserRole.ROLE_USER, UserStatus.ACTIVATED, pageable);
    }

    @Override
    public User getEmployeeById(String id) {
        return employeeRepository.findEmployAddresses(id).orElse(null);
    }

    @Transactional
    @Override
        public User createEmployee(User user, Address address) {
//    public User createEmployee(User user, Address address, MultipartFile file) {
        String rawPassword = PasswordUtil.generateRandomPassword(8); // Tạo mật khẩu 8 ký tự
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String userServiceName = userService.getName();

        // xử lý ảnh
//        String urlAvatar = null;
//        if (file != null && !file.isEmpty()) {
//            urlAvatar = uploadAvatarToCloudinary.uploadImage(file);
//        }

        // Tạo đối tượng user
        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setPassword(encodedPassword);
//        newUser.setAvatar(urlAvatar);
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setCitizenIdentity(user.getCitizenIdentity());
        newUser.setGender(user.getGender());
        newUser.setRoles(UserRole.ROLE_USER);
        newUser.setStatus(UserStatus.ACTIVATED);
        newUser.setCreatedBy(userServiceName);
        newUser.setCreateDate(new Date());

        // Lưu user và lấy ID mới
        User savedUser = employeeRepository.save(newUser);
        System.out.println("User ID: " + savedUser.getId()); // Debug xem có ID không

        if (address != null) {
            Address newAddress = new Address();
            newAddress.setAddressStatus(AddressStatus.DANG_SU_DUNG);
            newAddress.setProvinceId(address.getProvinceId());
            newAddress.setToDistrictId(address.getToDistrictId());
            newAddress.setWardCode(address.getWardCode());
            newAddress.setProvince(address.getProvince());
            newAddress.setDistrict(address.getDistrict());
            newAddress.setWard(address.getWard());
            newAddress.setLine(address.getLine());
            newAddress.setFullName(user.getFullName());
            newAddress.setPhoneNumber(user.getPhoneNumber());
            newUser.setCreateDate(new Date());
            newAddress.setUser(savedUser); // Không cần tìm lại user nữa

            // Lưu địa chỉ vào database
//            addressRepository.save(newAddress);
            Address savedAddress = addressRepository.save(newAddress);
            System.out.println("Address ID: " + savedAddress.getId()); // Debug xem có lưu không
        }
        System.out.println("Mật khẩu tài khoản mới: " + rawPassword);
//        String subject = "Xin chào, bạn đã đăng ký thành công tài khoản nhân viên CAPSTONE";
//        emailService.sendEmailPassword(newUser.getEmail(), subject, rawPassword);
        return savedUser;
    }

    @Transactional
    @Override
        public User updateEmployee(String id, User user, Address address) {
//    public User updateEmployee(String id, User user, Address address, MultipartFile file) {
        User existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee == null) {
            return null;
        }
        String userServiceName = userService.getName();

        // Nếu có file mới => Upload lên Cloudinary, ngược lại giữ nguyên ảnh cũ
//        String urlAvatar = existingEmployee.getAvatar(); // Giữ ảnh cũ
//        if (file != null && !file.isEmpty()) {
//            urlAvatar = uploadAvatarToCloudinary.uploadImage(file); // Upload ảnh mới
//        }

        // Cập nhật thông tin nhân viên
        existingEmployee.setFullName(user.getFullName());
        existingEmployee.setEmail(user.getEmail());
        existingEmployee.setPhoneNumber(user.getPhoneNumber());
        existingEmployee.setDateOfBirth(user.getDateOfBirth());
        existingEmployee.setGender(user.getGender());
        existingEmployee.setCitizenIdentity(user.getCitizenIdentity());
        existingEmployee.setStatus(user.getStatus());
//        existingEmployee.setAvatar(urlAvatar); // Cập nhật avatar
        existingEmployee.setUpdatedBy(userServiceName);
        existingEmployee.setLastModifiedDate(new Date());

        // Cập nhật hoặc thêm mới địa chỉ
        Address existingAddress = addressService.getDefaultAddress(existingEmployee.getId());
        if (existingAddress != null) {
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
            existingAddress.setLastModifiedDate(new Date());
            addressService.saveAddress(existingAddress);
        } else {
            Address newAddress = new Address();
            newAddress.setAddressStatus(AddressStatus.DANG_SU_DUNG);
            newAddress.setProvinceId(address.getProvinceId());
            newAddress.setToDistrictId(address.getToDistrictId());
            newAddress.setWardCode(address.getWardCode());
            newAddress.setProvince(address.getProvince());
            newAddress.setDistrict(address.getDistrict());
            newAddress.setWard(address.getWard());
            newAddress.setLine(address.getLine());
            newAddress.setFullName(user.getFullName());
            newAddress.setPhoneNumber(user.getPhoneNumber());
            newAddress.setCreatedBy(userServiceName);
            newAddress.setLastModifiedDate(new Date());
            newAddress.setUser(existingEmployee);

            if (existingEmployee.getAddresses() == null) {
                existingEmployee.setAddresses(new ArrayList<>());
            }
            existingEmployee.getAddresses().add(newAddress);
            addressService.saveAddress(newAddress);
        }

        // Lưu nhân viên
        return employeeRepository.save(existingEmployee);
    }

}
