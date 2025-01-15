package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.payload.user.UserRequest;

import java.util.List;

public interface UserService {
    void register(UserRequest userRequestDTO);

    User findUserByMemberEmail(String email);

    User getUserFromContext();

    List<User> findAllUser();
}
