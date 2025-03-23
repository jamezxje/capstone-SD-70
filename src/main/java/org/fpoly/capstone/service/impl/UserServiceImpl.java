package org.fpoly.capstone.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.config.UserDetailsCustom;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.enum_status.UserRole;
import org.fpoly.capstone.entity.enum_status.UserStatus;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.request.RegisterDTO;
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
    public void registerUser(RegisterDTO registerDTO) {
        try {
            User user = new User();
            user.setFullName(registerDTO.getFullName());
            user.setEmail(registerDTO.getEmail());
            user.setPhoneNumber(registerDTO.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            user.setStatus(UserStatus.ACTIVATED);
            user.setRoles(UserRole.ROLE_CUSTOMER);
            user.setCreateDate(new Date());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đăng ký tài khoản: " + e.getMessage());
        }
    }

}
