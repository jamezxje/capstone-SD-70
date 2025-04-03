package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;

import java.util.Optional;


public interface UserService {

    User getUserFromContext();

    String getName();

    User createUserRegister(User user);

    String processForgotPassword(String email);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCitizenIdentity(String citizenIdentity);
}
