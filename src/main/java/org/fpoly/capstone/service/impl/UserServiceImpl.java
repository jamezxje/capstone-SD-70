package org.fpoly.capstone.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.config.UserDetailsCustom;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.service.EmailService;
import org.fpoly.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

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
        String subject = "Chào mừng bạn đến với CAPSTONE! Tài khoản của bạn đã được tạo thành công. Đừng quên cập nhật thông tin để có trải nghiệm tốt nhất!";
        emailService.sendEmailPassword(newUser.getEmail(), subject, rawPassword);
        return newUser;
    }
}
