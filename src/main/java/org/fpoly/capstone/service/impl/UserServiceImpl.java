package org.fpoly.capstone.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.config.UserDetailsCustom;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Override
    public User getUserFromContext() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsCustom userDetailsCustom)) {
            return null;
        }

        if (userDetailsCustom.getUser() == null) {
            return null;
        }

        return userDetailsCustom.getUser();
    }

    @Override
    public String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userName;
    }

    @Override
    @Transactional
    public User createUserRegister(User user) {
        String rawPassword = user.getPassword();
        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setStatus(UserStatus.ACTIVATED);
        newUser.setRoles(UserRole.ROLE_CUSTOMER);
        newUser.setCreateDate(new Date());
        newUser.setLastModifiedDate(new Date());
        userRepository.save(newUser);
        String subject = "Chúc mừng! Bạn đã đăng ký thành công tài khoản CAPSTONE.";
        emailServiceImpl.sendEmailPassword(newUser.getEmail(), subject, rawPassword);
        return newUser;
    }

    private String generateRandomPassword() {
        int length = 10; // Độ dài mật khẩu
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

//    @Override
//    @Transactional
//    public String processForgotPassword(String email) {
//        Optional<User> userOptional = userRepository.findByEmailPassword(email);
//        if (userOptional.isEmpty()) {
//            return "Email không tồn tại trong hệ thống.";
//        }
//        User user = userOptional.get();
//        String newPassword = generateRandomPassword();
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodedPassword);
//        userRepository.save(user);
//        String subject = "Khôi phục mật khẩu - CAPSTONE";
//        emailService.sendEmailPassword(user.getEmail(), subject, newPassword);
//        return "Mật khẩu mới đã được gửi về email của bạn.";
//    }


    //validate
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.getUserByEmail(email).isPresent();
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.getUserBySDT(phoneNumber).isPresent();
    }

    @Override
    public boolean existsByCitizenIdentity(String citizenIdentity) {
        return userRepository.getUserByCCCD(citizenIdentity).isPresent();
    }
}
