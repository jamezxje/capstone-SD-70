package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.request.RegisterDTO;

public interface UserService {

    User getUserFromContext();
    String getName();
    void registerUser(RegisterDTO registerDTO);
}
