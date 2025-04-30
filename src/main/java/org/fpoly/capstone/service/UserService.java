package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;

import java.util.Map;
import java.util.Optional;


public interface UserService {

    User getUserFromContext();

    String getName();

    User createUserRegister(User user);
    Map<String, String> changeUserPassword(String currentPassword, String newPassword, String confirmPassword);
    String processForgotPassword(String email);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCitizenIdentity(String citizenIdentity);
}
