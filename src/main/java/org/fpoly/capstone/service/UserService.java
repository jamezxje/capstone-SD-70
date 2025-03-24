package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;


public interface UserService {

    User getUserFromContext();
    String getName();
    User createUserRegister(User user);
}
