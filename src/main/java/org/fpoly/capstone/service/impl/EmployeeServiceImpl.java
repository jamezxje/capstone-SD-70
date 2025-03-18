package org.fpoly.capstone.service.impl;

import jakarta.transaction.Transactional;
import org.fpoly.capstone.dto.user.AddressDTO;
import org.fpoly.capstone.dto.user.EmployeeDTO;
import org.fpoly.capstone.entity.Address;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.AddressStatus;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.repository.AddressRepository;
import org.fpoly.capstone.repository.EmployeeRepository;
import org.fpoly.capstone.service.EmployeeService;
import org.fpoly.capstone.service.payload.user.FileUploadImagesService;
//import org.fpoly.capstone.service.payload.user.SendEmailService;
import org.fpoly.capstone.service.payload.user.RandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

//    @Autowired
//    private SendEmailService sendEmailService;

    @Autowired
    private FileUploadImagesService fileUploadImagesService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllEmployees() {
        return employeeRepository.findByRolesAndStatus(UserRole.ROLE_USER, UserStatus.ACTIVATED);
    }

    @Override
    public User getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public User saveEmployee(User employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee object is null!");
        }
        System.out.println("Saving employee: " + employee);

        employee.setRoles(UserRole.ROLE_USER);
        return employeeRepository.save(employee);
    }

    @Transactional
    public User createEmployee(EmployeeDTO employeereRequest, AddressDTO addressRequest, MultipartFile file) {
        // Kiểm tra số điện thoại, email và căn cước công dân đã tồn tại chưa
        if (employeeRepository.getEmployBySDT(employeereRequest.getPhoneNumber()) != null) {
            throw new IllegalArgumentException("Số điện thoại này đã tồn tại!");
        }
        if (employeeRepository.getEmployByEmail(employeereRequest.getEmail()) != null) {
            throw new IllegalArgumentException("Email này đã tồn tại!");
        }
        if (employeeRepository.getEmployByCCCD(employeereRequest.getCitizenIdentity()) != null) {
            throw new IllegalArgumentException("Căn cước công dân này đã tồn tại!");
        }

        // Mã hóa mật khẩu
        String password = String.valueOf(new RandomNumberGenerator().generateRandom6DigitNumber());

        // Tạo đối tượng user
        User user = new User();
        user.setFullName(employeereRequest.getFullName());
        user.setPhoneNumber(employeereRequest.getPhoneNumber());
        user.setEmail(employeereRequest.getEmail());
        user.setStatus(employeereRequest.getStatus());
        user.setPassword(passwordEncoder.encode(password));
        user.setDateOfBirth(employeereRequest.getDateOfBirth());
        user.setGender(employeereRequest.getGender());
        user.setCitizenIdentity(employeereRequest.getCitizenIdentity());
        user.setRoles((UserRole.ROLE_USER));
        user.setStatus(UserStatus.ACTIVATED);

        // Lưu user vào database trước khi xử lý avatar
        user = employeeRepository.save(user);

        // Xử lý ảnh đại diện
        if (file != null && !file.isEmpty()) {
            String avatarFileName = fileUploadImagesService.saveAvatar(file, "employee", user.getId());
            user.setAvatar(avatarFileName);
            employeeRepository.save(user); // Cập nhật lại user với avatar
        }

        // Tạo địa chỉ cho user
        Address address = Address.builder()
                .status(AddressStatus.DANG_SU_DUNG)
                .ward(addressRequest.getWard())
                .toDistrictId(addressRequest.getToDistrictId())
                .provinceId(addressRequest.getProvinceId())
                .line(addressRequest.getLine())
                .province(addressRequest.getProvince())
                .district(addressRequest.getDistrict())
                .wardCode(addressRequest.getWardCode())
                .user(user)
                .build();

        addressRepository.save(address); // Lưu địa chỉ vào database

        // Gửi email thông báo tài khoản & mật khẩu
        String subject = "Xin chào, bạn đã đăng ký thành công tài khoản nhân viên CAPSTONE";
//        sendEmailService.sendEmailPassword(user.getEmail(), subject, employeereRequest.getPassword());

        return user;
    }

//    @Override
//    @Transactional
//    public User updateEmployee(Long id, User employee, UpdateAddressRequest addressRequest, MultipartFile avatarFile) {
//        // Kiểm tra nhân viên có tồn tại không
//        Optional<User> optionalUser = employeeRepository.findById(id);
//        if (!optionalUser.isPresent()) {
//            throw new RuntimeException("Nhân viên không tồn tại!");
//        }
//        User existingUser = optionalUser.get();
//
////        // Kiểm tra trùng số điện thoại
////        if (!existingUser.getPhoneNumber().equals(employee.getPhoneNumber()) &&
////                employeeRepository.getOneUserByPhoneNumber(employee.getPhoneNumber()) != null) {
////            throw new RuntimeException("Số điện thoại đã tồn tại!");
////        }
////
////        // Kiểm tra trùng email
////        if (!existingUser.getEmail().equals(employee.getEmail()) &&
////                employeeRepository.getOneUserByEmail(employee.getEmail()) != null) {
////            throw new RuntimeException("Email đã tồn tại!");
////        }
////
////        // Kiểm tra trùng CCCD
////        if (!existingUser.getCitizenIdentity().equals(employee.getCitizenIdentity()) &&
////                employeeRepository.getOneByCitizenIdentity(employee.getCitizenIdentity()) != null) {
////            throw new RuntimeException("Căn cước công dân đã tồn tại!");
////        }
//
//        // Cập nhật thông tin nhân viên
//        existingUser.setFullName(employee.getFullName());
//        existingUser.setPhoneNumber(employee.getPhoneNumber());
//        existingUser.setDateOfBirth(employee.getDateOfBirth());
//        existingUser.setEmail(employee.getEmail());
//        existingUser.setGender(employee.getGender());
//        existingUser.setStatus(employee.getStatus());
//        existingUser.setCitizenIdentity(employee.getCitizenIdentity());
//
//        // Cập nhật avatar nếu có
//        if (avatarFile != null && !avatarFile.isEmpty()) {
//            String avatarPath = fileUploadImagesService.saveAvatar(avatarFile, "employee", id);
//            if (avatarPath != null) {
//                existingUser.setAvatar(avatarPath);
//            }
//        }
//
//        employeeRepository.save(existingUser); // Lưu thông tin nhân viên vào database
//
//        // 🔥 Kiểm tra xem nhân viên có địa chỉ chưa
//        Address existingAddress = addressRepository.getAddressByUserIdAndStatus(existingUser.getId(), AddressStatus.DANG_SU_DUNG);
//
//        if (existingAddress != null) {
//            // Cập nhật địa chỉ hiện có
//            existingAddress.setId(existingUser.getId());
//            existingAddress.setProvince(addressRequest.getProvince());
//            existingAddress.setProvinceId(addressRequest.getProvinceId());
//            existingAddress.setDistrict(addressRequest.getDistrict());
//            existingAddress.setToDistrictId(
//                    addressRequest.getToDistrictId() != null ? String.valueOf(addressRequest.getToDistrictId()) : null
//            );
//            existingAddress.setWard(addressRequest.getWard());
//            existingAddress.setWardCode(addressRequest.getWardCode());
//            existingAddress.setLine(addressRequest.getLine());
//            existingAddress.setStatus(AddressStatus.DANG_SU_DUNG);
//        } else {
//            // Tạo địa chỉ mới nếu chưa có
//            existingAddress = new Address();
//            existingAddress.setUser(existingUser);
//            existingAddress.setProvince(addressRequest.getProvince());
//            existingAddress.setProvinceId(addressRequest.getProvinceId());
//            existingAddress.setDistrict(addressRequest.getDistrict());
//            existingAddress.setToDistrictId(
//                    addressRequest.getToDistrictId() != null ? String.valueOf(addressRequest.getToDistrictId()) : null
//            );
//            existingAddress.setWard(addressRequest.getWard());
//            existingAddress.setWardCode(addressRequest.getWardCode());
//            existingAddress.setLine(addressRequest.getLine());
//            existingAddress.setStatus(AddressStatus.DANG_SU_DUNG);
//        }
//
//        // Lưu địa chỉ sau khi đã cập nhật hoặc tạo mới
//        addressRepository.save(existingAddress);
//
//
//        return existingUser;
//    }

    @Override
    public User updateEmployee(Long id, User employee) {
        Optional<User> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            employee.setId(id);
            employee.setRoles(UserRole.ROLE_USER);
            return employeeRepository.save(employee);
        }
        return null;
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
