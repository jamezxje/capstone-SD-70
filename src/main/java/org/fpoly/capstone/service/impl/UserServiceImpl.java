package org.fpoly.capstone.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.config.auth.UserDetailsCustom;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.entity.type.Role;
import org.fpoly.capstone.repository.UserRepository;
import org.fpoly.capstone.service.UserService;
import org.fpoly.capstone.service.payload.user.UserRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRequest userRequestDTO) {
        User user = new User();

        user.setFirstname(userRequestDTO.getFirstName());
        user.setLastname(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(this.passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(Role.ROLE_CUSTOMER);

        this.userRepository.save(user);
    }

    @Override
    public User findUserByMemberEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetailsCustom userDetailsCustom = null;

        if (authentication != null) {
            userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        }

        assert userDetailsCustom != null;

        return userDetailsCustom.getUser();
    }

    @Override
    public List<User> findAllUser() {
        return this.userRepository.findAll();
    }

    private UserRequest toDTO(User user) {
        UserRequest userRequestDTO = new UserRequest();
        userRequestDTO.setFirstName(user.getFirstname());
        userRequestDTO.setLastName(user.getLastname());
        userRequestDTO.setEmail(user.getEmail());
        return userRequestDTO;
    }
}
