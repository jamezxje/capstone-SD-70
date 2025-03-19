package org.fpoly.capstone.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.fpoly.capstone.config.UserDetailsCustom;
import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

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

}
