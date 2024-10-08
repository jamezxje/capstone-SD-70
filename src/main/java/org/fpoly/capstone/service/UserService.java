package org.fpoly.capstone.service;

import org.fpoly.capstone.entity.User;
import org.fpoly.capstone.service.payload.user.UserRequestDTO;

import java.util.List;

public interface UserService {
  void register(UserRequestDTO userRequestDTO);

  User findUserByMemberEmail(String email);

  User getUserFromContext();

  List<User> findAllUser();
}
